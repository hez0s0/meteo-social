create database meteo;
grant usage on *.* to meteo@localhost identified by 'meteo';
grant all privileges on meteo.* to meteo@localhost;
flush privileges;


create database sonar;
grant usage on *.* to sonar@localhost identified by 'sonar';
grant all privileges on sonar.* to sonar@localhost;
flush privileges;
