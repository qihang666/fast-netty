@echo off  
echo ** setting runtime variable  
  
REM _protoSrc �����proto�ļ�Ŀ¼��λ��  
set _protoSrc=D:\\gameWork\\game-core\\protobuf
  
REM protoExe �����ڴ�proto���java��protoc.exe�����λ��  
set protoExe=D:\\gameWork\\game-core\\protobuf\protoc.exe
  
REM java_out_file �����ɵ�Java�ļ�Ŀ¼��λ��  
set java_out_file=D:\\gameWork\\game-core\\src\\main\\java
  
for /R "%_protoSrc%" %%i in (*) do (   
    set filename=%%~nxi   
    if "%%~xi"  == ".proto" (  
        %protoExe% --proto_path=%_protoSrc% --java_out=%java_out_file% %%i  
    )  
)
echo "----------SUCCEED---------"
pause  