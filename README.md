# devarchi33-bci-agent

1. pom.xml 에서 outputDirectory 와 manifestFile 의 경로를 알맞게 수정한다.
2. mvn clean install 실행.
3. classes/production/HelloBci/ 로 이동.
4. java -javaagent:devarchi33-bci-agent.jar HelloBci
