@echo off  
echo ** setting runtime variable  
  
REM _protoSrc �����proto�ļ�Ŀ¼��λ��  
set _protoSrc=D:\project\server\dough-base\protobuf\source
  
REM protoExe �����ڴ�proto���java��protoc.exe�����λ��  
set protoExe=D:\project\server\dough-base\protobuf\protoc.exe
  
REM java_out_file �����ɵ�Java�ļ�Ŀ¼��λ��  
set java_out_file=D:\project\server\dough-base\src\main\java
  
for /R "%_protoSrc%" %%i in (*) do (   
    set filename=%%~nxi   
    if "%%~xi"  == ".proto" (  
        %protoExe% --proto_path=%_protoSrc% --java_out=%java_out_file% %%i  
    )  
)
pause  