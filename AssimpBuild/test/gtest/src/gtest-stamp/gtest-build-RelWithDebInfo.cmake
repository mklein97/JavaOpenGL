

set(command "C:/Users/Matt/Documents/NetBeansProjects/cmake-3.9.1-win64-x64/bin/cmake.exe;--build;.;--config;RelWithDebInfo")
execute_process(
  COMMAND ${command}
  RESULT_VARIABLE result
  OUTPUT_FILE "C:/Users/Matt/Documents/NetBeansProjects/AssimpBuild/test/gtest/src/gtest-stamp/gtest-build-out.log"
  ERROR_FILE "C:/Users/Matt/Documents/NetBeansProjects/AssimpBuild/test/gtest/src/gtest-stamp/gtest-build-err.log"
  )
if(result)
  set(msg "Command failed: ${result}\n")
  foreach(arg IN LISTS command)
    set(msg "${msg} '${arg}'")
  endforeach()
  set(msg "${msg}\nSee also\n  C:/Users/Matt/Documents/NetBeansProjects/AssimpBuild/test/gtest/src/gtest-stamp/gtest-build-*.log")
  message(FATAL_ERROR "${msg}")
else()
  set(msg "gtest build command succeeded.  See also C:/Users/Matt/Documents/NetBeansProjects/AssimpBuild/test/gtest/src/gtest-stamp/gtest-build-*.log")
  message(STATUS "${msg}")
endif()
