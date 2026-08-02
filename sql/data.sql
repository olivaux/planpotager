INSERT INTO mydatabase.`Type` (name_type) VALUES
	 ('Fleurs'),
	 ('Fruits'),
	 ('Légumes'),
	 ('Plantes');
INSERT INTO mydatabase.Family (name_family,name_type) VALUES
	 ('Cucurbitacées','Légumes');
INSERT INTO mydatabase.Species (name_species,radius,name_icon,plantation_start,plantation_end,harvest_duration,name_family) VALUES
	 ('Citrouille',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Concombre',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Cornichon',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Courge',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Courgette',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Melon',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Pasteque',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Potimarron',10.0,NULL,4,10,6,'Cucurbitacées'),
	 ('Potiron',10.0,NULL,4,10,6,'Cucurbitacées');
INSERT INTO mydatabase.Variety (name_variety,radius,name_icon,plantation_start,plantation_end,harvest_duration,name_species) VALUES
	 ('Black beauty',10.0,NULL,4,10,6,'Courgette'),
	 ('Blanche d''égypte',10.0,NULL,4,10,6,'Courgette'),
	 ('Gold Rush',10.0,NULL,4,10,6,'Courgette'),
	 ('Ronde de nice',10.0,NULL,4,10,6,'Courgette');
INSERT INTO mydatabase.Association (name_species,name_species_1,isPositive) VALUES
	 ('Citrouille','Courgette',NULL),
	 ('Pasteque','Potiron',NULL);
