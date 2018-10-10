@echo off
set DIR=%~dp0
cd /d "%DIR%"
setlocal enabledelayedexpansion
for /r %%i in (*.proto) do ( 
set pbname=%%i 
      set pbname=!pbname:~0,-5!b
      protoc -I %DIR% --descriptor_set_out !pbname! %%i 
)
echo "----------SUCCEED---------"
pause  