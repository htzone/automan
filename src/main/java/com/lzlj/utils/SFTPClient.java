package com.lzlj.utils;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.lzlj.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Vector;

public class SFTPClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
      
    public static final String SFTP = "sftp";
    public static final String EXEC = "exec";
    
    private NumberFormat nf = new DecimalFormat("0.00");
    
    private Session session;  
    private String user;   
    private String pwd;  
    private String secretKeyFile;  
    private String host;  
    private int port;
    
    public static SFTPClient instance(String user, String pwd, String host, int port) {
    	return new SFTPClient(user, pwd, host, port);
    }
    
    public static SFTPClient instance(String user, String pwd, String host, int port, String secretKeyFile) {
    	return new SFTPClient(user, pwd, host, port, secretKeyFile);
    }
      
    private SFTPClient(String user, String pwd, String host, int port) {  
       this(user, pwd, host, port, null); 
    }  
  
    private SFTPClient(String user, String pwd, String host, int port, String secretKeyFile) {  
        this.user = user;  
        this.pwd = pwd;
        this.host = host;  
        this.port = port;  
        this.secretKeyFile = secretKeyFile;  
    }  
  
    /**
     * 登录主机
     * @return
     */
    public SFTPClient login() {
        try {  
            JSch jsch = new JSch();  
            if (secretKeyFile != null && secretKeyFile.trim().length() > 0) {  
                jsch.addIdentity(secretKeyFile);
                LOGGER.info("使用密钥文件 ：" + secretKeyFile);
            }  
            session = jsch.getSession(user, host, port);  
            if (pwd != null) {  
                session.setPassword(pwd);  
            }  
            Properties config = new Properties();  
            config.put("StrictHostKeyChecking", "no");  
            session.setConfig(config);  
            session.connect();
            LOGGER.info("登录主机成功：host = " + this.host + ", port = " + this.port);
        } catch (JSchException e) {
            LOGGER.error("登录失败", e);
        }
        return this;
    }  
  
    /**
     * 登出主机
     */
    public void logout() {  
        if (session != null) {  
            if (session.isConnected()) {  
                session.disconnect();  
            }  
        }
        this.session = null;
        LOGGER.info("登出主机成功：host = " + this.host + "，port = " + this.port);
    } 
    
    /**
     * 执行linux命令，并输出结果
     * @param cmd 命令
     * @return
     */
    public SFTPClient exec(String cmd) {
    	if (cmd == null || cmd.length() <= 0) {
    		return this;
    	}
        BufferedReader br = null;
        ChannelExec chl = this.openExecChannel(cmd);
        if (chl == null) {
        	return this;
        }
        try {
            InputStream in = chl.getInputStream();
            br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String buf = null;
            while ((buf = br.readLine()) != null) {
                LOGGER.info(buf);
            }
            LOGGER.info("执行命令成功：cmd = " + cmd);
        } catch (Exception e) {
            LOGGER.error("执行命令失败：cmd = " + cmd, e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // ignored
            }
            if (chl != null && chl.isConnected()) {
            	chl.disconnect();
            }
        }
        return this;
    }
  
    /**
     * 永久删除目录，慎用，如：rm -rf 文件或者目录
     * @param fileOrdir 文件或者目录绝对路径
     * @return
     */
    public SFTPClient rmFileOrDirPermanently(String fileOrdir) { 
    	if (fileOrdir == null || fileOrdir.trim().length() <= 0) {
    		return this;
    	}
    	this.exec("rm -rf " + fileOrdir);
        return this;
    } 
    
    /**
     * 递归创建目录，慎用，如：mkdir -p 目录
     * @param dir  目录绝对路径
     * @return
     */
    public SFTPClient mkdirRecursively(String dir) {
    	if (dir == null || dir.trim().length() <= 0) {
    		return this;
    	}
    	
    	ChannelSftp sftp = openSftpChannel();
    	if (sftp == null) {
    		return this;
    	}
    	
    	try { 
            sftp.cd(dir);  
        } catch (SftpException e) {
            LOGGER.error("目录不存在：dir = " + dir);
            try {
                LOGGER.info("准备创建：dir = " + dir);
                this.exec("mkdir -p " + dir);
                sftp.cd(dir);
            } catch (SftpException e2) {
                LOGGER.error("创建目录失败：dir = " + dir);
            }
    	} finally {
    		if (sftp != null && sftp.isConnected()) {
    			sftp.disconnect();
    		}
    	}
        return this;
    }  
  
    private ChannelSftp openSftpChannel() {
    	Channel channel = null;
    	try {
	    	channel = session.openChannel(SFTP);  
	        channel.connect();  
	        return (ChannelSftp) channel; 
    	} catch (Exception e) {
            LOGGER.error("打开SFTP失败", e);
    		try {} finally {
	    		if (channel != null && channel.isConnected()) {
	    			channel.disconnect();
	    		}
    		}
    		return null;
    	}
    }
    
    private ChannelExec openExecChannel(String cmd) {
    	Channel channel = null;
    	try {
    		channel = session.openChannel("exec");
    		ChannelExec chlExec = ((ChannelExec) channel);
    		chlExec.setCommand(cmd);
    		chlExec.setInputStream(null);
            chlExec.setErrStream(System.err);
            chlExec.connect();
            return chlExec;
    	} catch (Exception e) {
            LOGGER.error("打开EXEC失败", e);
    		try {} finally {
	    		if (channel != null && channel.isConnected()) {
	    			channel.disconnect();
	    		}
    		}
    		return null;
    	}
    }
    
    public SFTPSession getSFTPSession() {
    	return new SFTPSession();
    }
    
    public class SFTPSession {
    	private ChannelSftp sftp;
    	public SFTPSession() {
    		this.sftp = openSftpChannel();
    	}
    	
    	/**
         * 上传单个文件
         * @param dir  上传到的目录
         * @param file 要上传的文件
         * @return
         */
        public SFTPSession upload(String dir, final File file) {
        	if (sftp == null || dir == null || dir.trim().length() <= 0 || file == null || !file.exists()) {
        		return this;
        	}
        	
        	try {
                sftp.cd(dir);  
            } catch (SftpException e) {
                LOGGER.error("目录不存在：dir = " + dir);
                try {
                    LOGGER.info("准备创建：dir = " + dir);
                    exec("mkdir -p " + dir);
                    sftp.cd(dir);
                } catch (SftpException e2) {
                    LOGGER.error("创建目录失败：dir = " + dir);
                    return this;
                }
        	}
	    	
	    	FileInputStream fin = null;
	        try {  
	            fin = new FileInputStream(file);
	            final int totalCount = fin.available();
				sftp.put(fin, file.getName(), new SftpProgressMonitor() {
					private long uploadedCount = 0;
					@Override
                    public void init(int op, String src, String dest, long max) {
						uploadedCount = 0;
                        LOGGER.info("开始上传文件；file = " + file.getAbsolutePath());
					}
					@Override
                    public void end() {
                        LOGGER.info("上传文件；file = " + file.getAbsolutePath() + "-结束");
					}
					@Override
                    public boolean count(long count) {
						uploadedCount += count;
//    						LOG.info("已上传：" + uploadedCount + "个字节\t占比：" + nf.format((double)uploadedCount * 100.0 / (double)totalCount) + "%");
						return true;
					}
				});  
	        } catch (Exception e) {
                LOGGER.error("上传文件失败：file = " + file.getAbsolutePath(), e);
	        } finally {
	        	if (fin != null) {
	        		try {
	        			fin.close();
	        		} catch (Exception e) {
	        			// ignore
	        		}
	        	}
	        }
            return this;
        }
        
        /**
         * 下载单个文件
         * @param filePath  文件绝对路径
         * @param destFile  下载到文件绝对路径
         * @return
         */
        public SFTPSession download(final String filePath, String destFile){  
        	if (sftp == null || filePath == null || filePath.trim().length() <= 0
        			|| destFile == null || destFile.trim().length() <= 0) {
        		return this;
        	}
        	
        	FileOutputStream fout = null;
            try {  
                fout = new FileOutputStream(new File(destFile));
    			sftp.get(filePath, fout, new SftpProgressMonitor() {
    				private long downloadCount = 0;
    				@Override
                    public void init(int op, String src, String dest, long max) {
    					downloadCount = 0;
                        LOGGER.info("开始下载文件；file = " + filePath);
    				}
    				@Override
                    public void end() {
                        LOGGER.info("下载文件；file = " + filePath + "-结束");
    				}
    				@Override
                    public boolean count(long count) {
    					downloadCount += count;
//    					LOG.info("已下载：" + downloadCount + "个字节");
    					return true;
    				}
    			});  
            } catch (Exception e) {
                LOGGER.error("下载文件失败：file = " + filePath, e);
            } finally {
            	try {
            		if (fout != null) {
            			fout.close();
            		}
            	} catch (Exception e) {
            		// ignore
            	}
            }
            return this;
        } 
        
        /**
         * 删除单个文件
         * @param filePath  文件绝对路径
         * @return
         */
        public SFTPSession rmSingleFile(String filePath){  
        	ChannelSftp sftp = openSftpChannel();
        	if (sftp == null) {
        		return this;
        	}
        	
            try {  
                sftp.rm(filePath);
                LOGGER.error("删除文件成功：filePath = " + filePath);
            } catch (SftpException e) {
                LOGGER.error("删除文件失败：filePath = " + filePath, e);
            } finally {
            	
            }
            return this;
        }
    	
    	/**
         * 创建单个目录
         * @param dir 目录绝对路径
         * @return
         */
        public SFTPSession mkdir(String dir) { 
        	if (sftp == null) {
        		return this;
        	}
        	
            try {  
                sftp.mkdir(dir);
                LOGGER.error("创建文件夹成功：dir = " + dir);
            } catch (SftpException e) {
                LOGGER.error("创建文件夹失败：dir = " + dir, e);
            } finally {
            	
            }
            return this;
        } 
        
        /**
         * 删除空文件夹
         * @param dir 文件夹绝对路径
         * @return
         */
        public SFTPSession rmEmptyDir(String dir) { 
        	if (sftp == null) {
        		return this;
        	}
        	
            try {  
                sftp.rmdir(dir);
                LOGGER.error("删除文件夹成功：dir = " + dir);
            } catch (SftpException e) {
                LOGGER.error("删除文件夹失败：dir = " + dir, e);
            } finally {
            	
            }
            return this;
        }
        
        /**
         * 列举目录文件
         * @param dir 目录绝对路径
         * @return
         */
        @SuppressWarnings("unchecked")
    	public Vector<LsEntry> ls(String dir) {  
        	if (sftp == null) {
        		return null;
        	}
        	
            try {
    			return sftp.ls(dir);
    		} catch (SftpException e) {
                LOGGER.error("列举文件失败：dir = " + dir, e);
    		} finally {
            	
            }
            return null;
        }  
        
        public SFTPClient close() {
        	if (sftp != null && sftp.isConnected()) {
    			sftp.disconnect();
    		}
        	return SFTPClient.this;
        }
    }
    
    public static void main(String[] args) throws Exception {  
//    	SFTPClient sftp = SFTPClient.instance("root", "kuyou654@tcl.com", "10.73.128.99", 22)
//    		.login()
//    		.getSFTPSession()
//    		.upload("/opt/apache-tomcat-7.0.68/webapps", new File(Config.get("project.root.dir") + "/pricealarm-web/target/pricealarm.war"))
//    		.close();
//    	sftp.logout();
    }  
      
} 