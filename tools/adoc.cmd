@echo off
set ERROR_CODE=0

SET JARFILE=
FOR %%I in (adocer-*.jar) DO SET JARFILE=%%I 

if "%JARFILE%" == "" goto jarnotfound
java -Dfile.encoding=UTF-8 -jar %~dp0%JARFILE% %*

goto end

:jarnotfound
echo Jarfile not found in "%~dp0"

:error
set ERROR_CODE=1

:end
exit /B %ERROR_CODE%