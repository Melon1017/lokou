call cd ..
call mvn clean:clean
call mvn -Dmaven.test.skip=true package 
call mvn assembly:assembly
@pause