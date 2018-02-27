CREATE TABLE Classes (
	class TEXT PRIMARY KEY,
	"type" TEXT CHECK ("type" = 'bb' OR "type" = 'bc'),
	country TEXT,
	numGuns NUMERIC,
	bore NUMERIC,
	displacement NUMERIC
	);

CREATE TABLE Battles (
	name TEXT PRIMARY KEY,
	"date" DATERANGE
	);

CREATE TABLE Ships (
	name TEXT PRIMARY KEY,
	class TEXT REFERENCES Classes,
	launched NUMERIC(4) CHECK (launched <= 1945) 
	);

CREATE TABLE Outcomes (
	ship TEXT REFERENCES Ships,
	battle TEXT REFERENCES Battles,
	result TEXT CHECK (result = 'ok' OR result = 'sunk' OR result = 'damaged'),
	PRIMARY KEY (ship, battle)
	);

-- Sample data for Classes

INSERT INTO Classes VALUES ('Bismarck', 'bb', 'Germany', 8, 15, 42000);
INSERT INTO Classes VALUES ('Iowa', 'bb', 'USA', 9, 16, 46000);
INSERT INTO Classes VALUES ('Kongo', 'bc', 'Japan', 8, 14, 32000);
INSERT INTO Classes VALUES ('North Carolina', 'bb', 'USA', 9, 16, 37000);
INSERT INTO Classes VALUES ('Renown', 'bc', 'Gt. Britain', 6, 15, 32000);
INSERT INTO Classes VALUES ('Revenge', 'bb', 'Gt. Britain', 8, 15, 29000);
INSERT INTO Classes VALUES ('Tennessee', 'bb', 'USA', 12, 14, 32000);
INSERT INTO Classes VALUES ('Yamato', 'bb', 'Japan', 9, 18, 65000);

-- Sample data for Battles

INSERT INTO Battles VALUES ('Denmark Strait', '[1941-05-24, 1941-05-27]'::daterange);
INSERT INTO Battles VALUES ('Guadalcanal', '[1942-11-15, 1942-11-15]'::daterange);
INSERT INTO Battles VALUES ('North Cape', '[1943-12-26, 1943-12-26]'::daterange);
INSERT INTO Battles VALUES ('Surigao Strait', '[1944-10-25, 1944-10-25]'::daterange);

-- Sample data for Ships

INSERT INTO Ships VALUES ('California', 'Tennessee', 1921);
INSERT INTO Ships VALUES ('Haruna', 'Kongo', 1915);
INSERT INTO Ships VALUES ('Hiei', 'Kongo', 1914);
INSERT INTO Ships VALUES ('Iowa', 'Iowa', 1943);
INSERT INTO Ships VALUES ('Kirishima', 'Kongo', 1915);
INSERT INTO Ships VALUES ('Kongo', 'Kongo', 1913);
INSERT INTO Ships VALUES ('Missouri', 'Iowa', 1944);
INSERT INTO Ships VALUES ('Musashi', 'Yamato', 1942);
INSERT INTO Ships VALUES ('New Jersey', 'Iowa', 1943);
INSERT INTO Ships VALUES ('North Carolina', 'North Carolina', 1941);
INSERT INTO Ships VALUES ('Ramillies', 'Revenge', 1917);
INSERT INTO Ships VALUES ('Renown', 'Renown', 1916);
INSERT INTO Ships VALUES ('Repulse', 'Renown', 1916);
INSERT INTO Ships VALUES ('Resolution', 'Revenge', 1916);
INSERT INTO Ships VALUES ('Revenge', 'Revenge', 1916);
INSERT INTO Ships VALUES ('Royal Oak', 'Revenge', 1916);
INSERT INTO Ships VALUES ('Royal Sovereign', 'Revenge', 1916);
INSERT INTO Ships VALUES ('Tennessee', 'Tennessee', 1920);
INSERT INTO Ships VALUES ('Washington', 'North Carolina', 1941);
INSERT INTO Ships VALUES ('Wisconsin', 'Iowa', 1944);
INSERT INTO Ships VALUES ('Yamato', 'Yamato', 1941);

-- Sample data for Outcomes

-- INSERT INTO Outcomes VALUES ('Arizona', 'Pearl Harbor', 'sunk');
-- INSERT INTO Outcomes VALUES ('Bismarck', 'Denmark Strait', 'sunk');
INSERT INTO Outcomes VALUES ('California', 'Surigao Strait', 'ok');
-- INSERT INTO Outcomes VALUES ('Duke of York', 'North Cape', 'ok');
-- INSERT INTO Outcomes VALUES ('Fuso', 'Surigao Strait', 'sunk');
-- INSERT INTO Outcomes VALUES ('Hood', 'Denmark Strait', 'sunk');
-- INSERT INTO Outcomes VALUES ('King George V', 'Denmark Strait', 'ok');
INSERT INTO Outcomes VALUES ('Kirishima', 'Guadalcanal', 'sunk');
-- INSERT INTO Outcomes VALUES ('Prince of Wales', 'Denmark Strait', 'damaged');
-- INSERT INTO Outcomes VALUES ('Rodney', 'Denmark Strait', 'ok');
-- INSERT INTO Outcomes VALUES ('Scharnhorst', 'North Cape', 'sunk');
-- INSERT INTO Outcomes VALUES ('South Dakota', 'Guadalcanal', 'damaged');
INSERT INTO Outcomes VALUES ('Tennessee', 'Surigao Strait', 'ok');
INSERT INTO Outcomes VALUES ('Washington', 'Guadalcanal', 'ok');
-- INSERT INTO Outcomes VALUES ('West Virginia', 'Surigao Strait', 'ok');
-- INSERT INTO Outcomes VALUES ('Yamashiro', 'Surigao Strait', 'sunk');
