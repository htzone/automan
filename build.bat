@echo off
echo.
echo [��Ϣ] ������̣�����war/jar���ļ���
echo.

call mvn clean install -Dmaven.test.skip=true

pause