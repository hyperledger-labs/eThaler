@echo off
REM Copyright 2020 Swapshub
REM Licensed under the Apache License, Version 2.0 (the "License");
REM you may not use this file except in compliance with the License.
REM You may obtain a copy of the License at
REM http://www.apache.org/licenses/LICENSE-2.0

@echo off
@rem echo Starting eThalerClient


set DIRNAME=%~dp0

@rem echo %DIRNAME%

set CMD_LINE_ARGS= %DIRNAME% %*

@rem change directory
@rem cd build/runeThalerClient
call  ./build/runeThalerClient/eThalerClient.bat %CMD_LINE_ARGS%