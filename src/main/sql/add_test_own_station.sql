--insert the station model of our station
INSERT INTO metStationModel (name, description) VALUES ('Dummy station', 'A non real station meant for debugging based on simple CSV files accesible via local folder');

--insert the own station
INSERT INTO metStation (name, own) VALUES ('Gustavo', true);

--insert the config parameters of the own station
INSERT INTO metParameter (name, description, defaultValue, stationId) VALUES ('Folder', 'The folder where the observations file is to be located', '/home/dummy/observations', (SELECT id FROM metStation WHERE own = true));
INSERT INTO metParameter (name, description, defaultValue, stationId) VALUES ('File', 'Name of the file where the observations are included', 'dummy_observations.csv', (SELECT id FROM metStation WHERE own = true));

--insert the variables of the own station
INSERT INTO metVariable (name, acronym, description, unit, defaultMaximum, defaultMinimum, physicalMaximum, physicalMinimum, stationId) VALUES ('Temperature', 'Temp', 'The air temperature', 'ºC', 50, -30, 80, -80, (SELECT id FROM metStation WHERE own = true));
INSERT INTO metVariable (name, acronym, description, unit, defaultMaximum, defaultMinimum, physicalMaximum, physicalMinimum, stationId) VALUES ('Pressure', 'Press', 'The air pressure', 'mbar', 1100, 850, 1500, 500, (SELECT id FROM metStation WHERE own = true));
INSERT INTO metVariable (name, acronym, description, unit, defaultMaximum, defaultMinimum, physicalMaximum, physicalMinimum, stationId) VALUES ('Humidity', 'Hum', 'Relative humidity of the air', '%', 90, 0, 100, 0, (SELECT id FROM metStation WHERE own = true));
INSERT INTO metVariable (name, acronym, description, unit, defaultMaximum, defaultMinimum, physicalMaximum, physicalMinimum, stationId) VALUES ('Wind speed', 'WindS', 'Speed of the wind', 'm/s', 50, 0, 200, 0, (SELECT id FROM metStation WHERE own = true));
INSERT INTO metVariable (name, acronym, description, unit, defaultMaximum, defaultMinimum, physicalMaximum, physicalMinimum, stationId) VALUES ('Wind direction', 'WindD', 'Direction of the wind in grades', 'º', 360, 0, 360, 0, (SELECT id FROM metStation WHERE own = true));
