insert into Customer (id, email, firstname, lastname) values (1, 'dave@dmband1.com', 'Dave', 'Matthews1')
insert into Customer (id, email, firstname, lastname) values (2, 'carter@dmband2.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (3, 'boyd@dmband3.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (4, 'dave@dmband4.com', 'Dave', 'Matthews2')
insert into Customer (id, email, firstname, lastname) values (5, 'carter@dmband5.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (6, 'boyd@dmband6.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (7, 'dave@dmband7.com', 'Dave', 'Matthews3')
insert into Customer (id, email, firstname, lastname) values (8, 'carter@dmband8.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (9, 'boyd@dmband9.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (10, 'dave@dmband10.com', 'Dave', 'Matthews4')
insert into Customer (id, email, firstname, lastname) values (11, 'carter@dmband11.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (12, 'boyd@dmband12.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (13, 'dave@dmband13.com', 'Dave', 'Matthews5')
insert into Customer (id, email, firstname, lastname) values (14, 'carter@dmband14.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (15, 'boyd@dmband15.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (16, 'dave@dmband16.com', 'Dave', 'Matthews6')
insert into Customer (id, email, firstname, lastname) values (17, 'carter@dmband17.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (18, 'boyd@dmband18.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (19, 'dave@dmband19.com', 'Dave', 'Matthews7')
insert into Customer (id, email, firstname, lastname) values (20, 'carter@dmband20.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (21, 'boyd@dmband21.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (22, 'dave@dmband22.com', 'Dave', 'Matthews8')
insert into Customer (id, email, firstname, lastname) values (23, 'carter@dmband23.com', 'Carter', 'Beauford');
insert into Customer (id, email, firstname, lastname) values (24, 'boyd@dmband24.com', 'Boyd', 'Tinsley');
insert into Customer (id, email, firstname, lastname) values (25, 'boyd@dmband25.com', 'Boyd', 'Matthews1');

insert into Address (id, street, city, country, customer_id) values (1, '27 Broadway', 'New York', 'United States', 1);
insert into Address (id, street, city, country, customer_id) values (2, '27 Broadway', 'New York', 'United States', 1);

insert into Product (id, name, description, price) values (1, 'iPad', 'Apple tablet device', 499.0);
insert into Product (id, name, description, price) values (2, 'MacBook Pro', 'Apple notebook', 1299.0);
insert into Product (id, name, description, price) values (3, 'Dock', 'Dock for iPhone/iPad', 49.0);

insert into Product_Attributes (attributes_key, product_id, attributes) values ('connector', 1, 'socket');
insert into Product_Attributes (attributes_key, product_id, attributes) values ('connector', 3, 'plug');

insert into Orders (id, customer_id, billingaddress_id, shippingaddress_id) values (1, 1, 1, 2);
insert into Orders (id, customer_id, billingaddress_id, shippingaddress_id) values (2, 3, 2, 1);
insert into LineItem (id, product_id, amount, order_id, price) values (1, 1, 2, 1, 499.0);
insert into LineItem (id, product_id, amount, order_id, price) values (2, 2, 1, 1, 1299.0);

insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (1, 'integer search test', 1234567, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (2, 'decimal search test', 456, 1499.99);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (3, '#<,&«$''(*@×¢¤₤€¥ª ™®© ØøÅåÆæĈę ¦_{~>½', 789, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (4, 'Case Sensitivity Test', 901, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (5, 'Case sensitivity test', 901, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (6, 'query partial search test', 234, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (7, 'partial querysearch test', 345, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, decimalfield) values (8, 'search test by partial query', 567, 22.2);

insert into ParentTestEntity (id, name) values (1, 'Parent Item1: 100 complex items');
insert into ParentTestEntity (id, name) values (2, 'Parent Item2: no complex items');
insert into ParentTestEntity (id, name) values (3, 'Parent Item3');

insert into ChildTestEntity (id, name, parent_id) values (1, 'Child Item 1', 1);
insert into ChildTestEntity (id, name, parent_id) values (2, 'Child Item 2', 2);
insert into ChildTestEntity (id, name, parent_id) values (3, 'Child Item 3', 3);

insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (1, 'Parent1.Entity 1', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (2, 'Parent1.Entity 2', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (3, 'Parent1.Entity 3', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (4, 'Parent1.Entity 4', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (5, 'Parent1.Entity 5', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (6, 'Parent1.Entity 6', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (7, 'Parent1.Entity 7', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (8, 'Parent1.Entity 8', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (9, 'Parent1.Entity 9', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (10, 'Parent1.Entity 10', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (11, 'Parent1.Entity 11', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (12, 'Parent1.Entity 12', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (13, 'Parent1.Entity 13', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (14, 'Parent1.Entity 14', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (15, 'Parent1.Entity 15', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (16, 'Parent1.Entity 16', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (17, 'Parent1.Entity 17', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (18, 'Parent1.Entity 18', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (19, 'Parent1.Entity 19', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (20, 'Parent1.Entity 20', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (21, 'Parent1.Entity 21', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (22, 'Parent1.Entity 22', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (23, 'Parent1.Entity 23', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (24, 'Parent1.Entity 24', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (25, 'Parent1.Entity 25', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (26, 'Parent1.Entity 26', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (27, 'Parent1.Entity 27', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (28, 'Parent1.Entity 28', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (29, 'Parent1.Entity 29', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (30, 'Parent1.Entity 30', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (31, 'Parent1.Entity 31', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (32, 'Parent1.Entity 32', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (33, 'Parent1.Entity 33', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (34, 'Parent1.Entity 34', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (35, 'Parent1.Entity 35', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (36, 'Parent1.Entity 36', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (37, 'Parent1.Entity 37', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (38, 'Parent1.Entity 38', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (39, 'Parent1.Entity 39', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (40, 'Parent1.Entity 40', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (41, 'Parent1.Entity 41', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (42, 'Parent1.Entity 42', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (43, 'Parent1.Entity 43', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (44, 'Parent1.Entity 44', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (45, 'Parent1.Entity 45', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (46, 'Parent1.Entity 46', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (47, 'Parent1.Entity 47', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (48, 'Parent1.Entity 48', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (49, 'Parent1.Entity 49', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (50, 'Parent1.Entity 50', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (51, 'Parent1.Entity 51', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (52, 'Parent1.Entity 52', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (53, 'Parent1.Entity 53', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (54, 'Parent1.Entity 54', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (55, 'Parent1.Entity 55', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (56, 'Parent1.Entity 56', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (57, 'Parent1.Entity 57', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (58, 'Parent1.Entity 58', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (59, 'Parent1.Entity 59', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (60, 'Parent1.Entity 60', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (61, 'Parent1.Entity 61', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (62, 'Parent1.Entity 62', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (63, 'Parent1.Entity 63', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (64, 'Parent1.Entity 64', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (65, 'Parent1.Entity 65', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (66, 'Parent1.Entity 66', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (67, 'Parent1.Entity 67', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (68, 'Parent1.Entity 68', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (69, 'Parent1.Entity 69', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (70, 'Parent1.Entity 70', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (71, 'Parent1.Entity 71', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (72, 'Parent1.Entity 72', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (73, 'Parent1.Entity 73', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (74, 'Parent1.Entity 74', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (75, 'Parent1.Entity 75', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (76, 'Parent1.Entity 76', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (77, 'Parent1.Entity 77', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (78, 'Parent1.Entity 78', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (79, 'Parent1.Entity 79', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (80, 'Parent1.Entity 80', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (81, 'Parent1.Entity 81', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (82, 'Parent1.Entity 82', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (83, 'Parent1.Entity 83', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (84, 'Parent1.Entity 84', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (85, 'Parent1.Entity 85', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (86, 'Parent1.Entity 86', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (87, 'Parent1.Entity 87', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (88, 'Parent1.Entity 88', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (89, 'Parent1.Entity 89', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (90, 'Parent1.Entity 90', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (91, 'Parent1.Entity 91', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (92, 'Parent1.Entity 92', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (93, 'Parent1.Entity 93', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (94, 'Parent1.Entity 94', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (95, 'Parent1.Entity 95', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (96, 'Parent1.Entity 96', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (97, 'Parent1.Entity 97', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (98, 'Parent1.Entity 98', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (99, 'Parent1.Entity 99', 1, 3);
insert into ComplexDataTypeEntity (id, name, parent_id, child_id) values (100, 'Parent1.Entity 100', 1, 3);
