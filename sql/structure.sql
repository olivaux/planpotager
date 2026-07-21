CREATE TABLE User_(
   email VARCHAR(50) ,
   unit VARCHAR(2) ,
   provider VARCHAR(20) ,
   provider_id VARCHAR(255) ,
   language_ VARCHAR(2) ,
   PRIMARY KEY(email)
);

CREATE TABLE Garden(
   id_garden INT AUTO_INCREMENT,
   location_Longitude DECIMAL(9,7)  ,
   location_Latitude DECIMAL(9,7)  ,
   email VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_garden),
   FOREIGN KEY(email) REFERENCES User_(email)
);

CREATE TABLE Area(
   id_area INT AUTO_INCREMENT,
   point_leftUp_x DECIMAL(5,2)  ,
   point_leftUp_y DECIMAL(5,2)  ,
   point_rightUp_x DECIMAL(5,2)  ,
   point_rightUp_y DECIMAL(5,2)  ,
   point_rightDown_x DECIMAL(5,2)  ,
   point_rightDown_y DECIMAL(5,2)  ,
   point_leftDown_x DECIMAL(5,2)  ,
   point_leftDown_y DECIMAL(5,2)  ,
   id_garden INT NOT NULL,
   PRIMARY KEY(id_area),
   FOREIGN KEY(id_garden) REFERENCES Garden(id_garden)
);

CREATE TABLE Type(
   name_type VARCHAR(50) ,
   PRIMARY KEY(name_type)
);

CREATE TABLE Family(
   name_family VARCHAR(50) ,
   name_type VARCHAR(50)  NOT NULL,
   PRIMARY KEY(name_family),
   FOREIGN KEY(name_type) REFERENCES Type(name_type)
);

CREATE TABLE Species(
   name_species VARCHAR(50) ,
   radius DECIMAL(5,2)  ,
   name_icon VARCHAR(50) ,
   plantation_start TINYINT,
   plantation_end TINYINT,
   harvest_start TINYINT,
   harvest_end TINYINT,
   name_family VARCHAR(50)  NOT NULL,
   PRIMARY KEY(name_species),
   FOREIGN KEY(name_family) REFERENCES Family(name_family)
);

CREATE TABLE Variety(
   name_variety VARCHAR(50) ,
   radius DECIMAL(5,2)  ,
   name_icon VARCHAR(50) ,
   plantation_start TINYINT,
   plantation_end TINYINT,
   harvest_start TINYINT,
   harvest_end TINYINT,
   name_species VARCHAR(50)  NOT NULL,
   PRIMARY KEY(name_variety),
   FOREIGN KEY(name_species) REFERENCES Species(name_species)
);

CREATE TABLE SeedPacket(
   id_seedpacket INT AUTO_INCREMENT,
   brand VARCHAR(50) ,
   date_expiration DATE,
   reference VARCHAR(50) ,
   name_variety VARCHAR(50)  NOT NULL,
   email VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_seedpacket),
   FOREIGN KEY(name_variety) REFERENCES Variety(name_variety),
   FOREIGN KEY(email) REFERENCES User_(email)
);

CREATE TABLE Article(
   id_article INT AUTO_INCREMENT,
   title VARCHAR(100)  NOT NULL,
   link VARCHAR(100) ,
   name_content VARCHAR(50) ,
   name_type VARCHAR(50) ,
   name_family VARCHAR(50) ,
   name_species VARCHAR(50) ,
   name_variety VARCHAR(50) ,
   PRIMARY KEY(id_article),
   FOREIGN KEY(name_type) REFERENCES Type(name_type),
   FOREIGN KEY(name_family) REFERENCES Family(name_family),
   FOREIGN KEY(name_species) REFERENCES Species(name_species),
   FOREIGN KEY(name_variety) REFERENCES Variety(name_variety)
);

CREATE TABLE Plant(
   id_plant INT AUTO_INCREMENT,
   position_ GEOMETRY,
   date_toPlant DATE,
   date_planted DATE,
   date_toHarvest DATE,
   date_harvested DATE,
   id_garden INT NOT NULL,
   id_seedpacket INT NOT NULL,
   PRIMARY KEY(id_plant),
   FOREIGN KEY(id_garden) REFERENCES Garden(id_garden),
   FOREIGN KEY(id_seedpacket) REFERENCES SeedPacket(id_seedpacket)
);

CREATE TABLE Notification(
   id_notif INT AUTO_INCREMENT,
   email VARCHAR(50)  NOT NULL,
   isRead BOOLEAN,
   message VARCHAR(50) ,
   type VARCHAR(50) ,
   created_at DATETIME,
   PRIMARY KEY(id_notif),
   FOREIGN KEY(email) REFERENCES User_(email)
);

CREATE TABLE Association(
   name_species VARCHAR(50) ,
   name_species_1 VARCHAR(50) ,
   isPositive BOOLEAN,
   PRIMARY KEY(name_species, name_species_1),
   FOREIGN KEY(name_species) REFERENCES Species(name_species),
   FOREIGN KEY(name_species_1) REFERENCES Species(name_species)
);
