@echo off
echo.
echo [信息] 打包工程，生成war/jar包文件。
echo.

call mvn clean install -Dmaven.test.skip=true

pause