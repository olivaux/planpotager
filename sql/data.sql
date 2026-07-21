INSERT INTO mydatabase.`Type` (name_type) VALUES
	 ('Fleurs'),
	 ('Fruits'),
	 ('Légumes'),
	 ('Plantes');
INSERT INTO mydatabase.Family (name_family,name_type) VALUES
	 ('Cucurbitacées','Légumes');
INSERT INTO mydatabase.Species (name_species,radius,name_icon,plantation_start,plantation_end,harvest_start,harvest_end,name_family) VALUES
	 ('Citrouille',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Concombre',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Cornichon',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Courge',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Courgette',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Melon',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Pasteque',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Potimarron',10.0,NULL,4,10,6,12,'Cucurbitacées'),
	 ('Potiron',10.0,NULL,4,10,6,12,'Cucurbitacées');
INSERT INTO mydatabase.Variety (name_variety,radius,name_icon,plantation_start,plantation_end,harvest_start,harvest_end,name_species) VALUES
	 ('Black beauty',NULL,NULL,NULL,NULL,NULL,NULL,'Courgette'),
	 ('Blanche d''égypte',NULL,NULL,NULL,NULL,NULL,NULL,'Courgette'),
	 ('Gold Rush',NULL,NULL,NULL,NULL,NULL,NULL,'Courgette'),
	 ('Ronde de nice',NULL,NULL,NULL,NULL,NULL,NULL,'Courgette');
INSERT INTO mydatabase.Association (name_species,name_species_1,isPositive) VALUES
	 ('Citrouille','Courgette',NULL),
	 ('Pasteque','Potiron',NULL);
