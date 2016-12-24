@echo off 
@title AuroStory Quick Quest Creator 
set CLASSPATH=.;lib\* 
java -Xmx10m -Djavax.net.ssl.keyStore=filename.keystore -Djavax.net.ssl.keyStorePassword=passwd -Djavax.net.ssl.trustStore=filename.keystore -Djavax.net.ssl.trustStorePassword=passwd tools.QuestCreator 
pause  