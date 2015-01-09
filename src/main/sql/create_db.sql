create database meteo;
grant usage on *.* to meteo@localhost identified by 'meteo';
grant all privileges on meteo.* to meteo@localhost;
flush privileges;
