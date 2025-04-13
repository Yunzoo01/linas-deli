--Category
INSERT INTO category (category_id, category_name) VALUES
(1, 'Meat'),
(2, 'Cheese'),
(3, 'Others'),
(4, 'Bulk');

--supplier
INSERT INTO supplier (supplier_name, contact_name, s_email, s_phone, note) VALUES
('Tierney''s', 'Blair Dirk', 'info@tierneyswholesale.com', '6042735408', NULL),
('Saputo', 'Sydney Wong', 'sydney.wong@saputo.com', '7787088789', 'delivery: Thu & Sat min. $ 200'),
('Lekker', 'Stephanie Michaels', 'stephaniem@lekkerfoods.com', '2508866170', 'Wed& Sat min400\ncut off 12:30'),
('Valoroso', 'Alex', NULL, '7789550434', 'tue min300'),
('Living Tree', 'Emily Wilson', 'emily.wilson@livingtreefoods.ca', NULL, NULL),
('Krinos', 'Joe Perri', 'joep@krinos.ca', '7788403822', NULL),
('BOSA', 'Order Desk', 'orderdesk@bosafoods.com', '6042535578', '778-384-3522 delivery: tue& Fri  min 400'),
('Tree Of Life', 'Micheal Koo', 'Michael.Koo@treeoflife.com', NULL, 'monday and thursday $700\nSubmit order by thursday noon for monday order by tuesday noon for thursday'),
('Freybe', 'Karoline MacKenzie', 'Kmackenzie@freybe.com', '6046077426', NULL),
('Macchi', 'Courtney Royer', 'oriol.t@macchiinc.com', '7802646206', NULL),
('Continental Sausage', 'William Kaminski', 'williamk@continentalsausage.ca', '6048746240', '6044884392'),
('Black Forest Meat & Sausage', 'Andréas', 'blackforest@bfmeats.ca', '6049801625', NULL),
('Miraco', 'Todor Todorov', 'info@miracoeurofood.com', '6048363816', NULL),
('La Grotta', 'Corrie Murray', 'corrie@lgdf.ca', '6042150046', 'delivery: Wednesday\n4036171400 Aqsa'),
('Continental Importers', 'Matt Cane', 'matt@contientalimporters.ca', '6045620322', 'min 200'),
('French Quality Imports', 'Charles Rech', 'charles@frenchqualityimports.com', '7788594049', NULL),
('Agropur', 'Mindy Hiu', 'mindy.hui@agropur.com', '6043414718', NULL),
('Les Dependances', 'Florence Urbin', 'orders@lesdependances.com', '4506566661', NULL),
('Two Rivers', 'Fraser Mittlestead', 'ar@tworiversmeats.com', '6049905288', NULL),
('Mt Lehman', 'Jason Dykstra', 'orders@mtlehmancheese.ca', NULL , NULL),
('Dare Foods', 'Cindi Wicklund', 'cwicklund@darefoods.com', '5198933233', NULL),
('WOW Foods', 'Flavio Assis', 'info@wowfood.ca', '7783183017', NULL),
('Lina''s Deli', 'Roman Lerner', 'kaisereck@gmail.com', '6043291141', NULL);

--country
INSERT INTO country (country_id, country_name) VALUES
(1, 'Canada'),
(2, 'United States'),
(3, 'France'),
(4, 'Italy'),
(5, 'Switzerland'),
(6, 'Netherlands'),
(7, 'United Kingdom'),
(8, 'Germany'),
(9, 'Denmark'),
(10, 'Spain'),
(11, 'Portugal'),
(12, 'Greece'),
(13, 'Norway'),
(14, 'Sweden'),
(15, 'Australia'),
(16, 'Argentina'),
(17, 'Belgium'),
(18, 'Austria'),
(19, 'Serbia');

--animal
INSERT INTO animal (animal_id, animal_name) VALUES
(1, 'Cow'),
(2, 'Sheep'),
(3, 'Goat'),
(4, 'Buffalo'),
(5, 'Sheep + Goat'),
(6, 'Pork'),
(7, 'Beef'),
(8, 'Chicken'),
(9, 'Lamb'),
(10, 'Bison'),
(11, 'Elk'),
(12, 'Turkey');

