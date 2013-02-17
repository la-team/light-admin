insert into Customer (id, email, firstname, lastname) values (1, 'dave@dmband1.com', 'Dave', 'One')
insert into Customer (id, email, firstname, lastname) values (2, 'carter@dmband2.com', 'Carter', 'Two');
insert into Customer (id, email, firstname, lastname) values (3, 'martin@dmband3.com', 'Martin', 'Three');
insert into Customer (id, email, firstname, lastname) values (4, 'caroline@dmband4.com', 'Caroline', 'Four')
insert into Customer (id, email, firstname, lastname) values (5, 'gavin@dmband5.com', 'Gavin', 'Five');
insert into Customer (id, email, firstname, lastname) values (6, 'luzek@dmband6.com', 'Luzek', 'Six');
insert into Customer (id, email, firstname, lastname) values (7, 'karmak@dmband7.com', 'Karmak', 'Seven')
insert into Customer (id, email, firstname, lastname) values (8, 'maxim@dmband8.com', 'Maxim', 'Eight');
insert into Customer (id, email, firstname, lastname) values (9, 'irina@dmband9.com', 'Irina', 'Nine');
insert into Customer (id, email, firstname, lastname) values (10, 'tolik@dmband10.com', 'Tolik', 'Ten')
insert into Customer (id, email, firstname, lastname) values (11, 'sasha@dmband11.com', 'Sasha', 'Eleven');
insert into Customer (id, email, firstname, lastname) values (12, 'pasha@dmband12.com', 'Pasha', 'Twelve');

insert into Address (id, street, city, country, customer_id) values (1, 'New York', 'New York', 'United States', 1);
insert into Address (id, street, city, country, customer_id) values (2, 'London', 'London', 'England', 1);
insert into Address (id, street, city, country, customer_id) values (3, 'Kiev', 'Kiev', 'Ukraine', 2);
insert into Address (id, street, city, country, customer_id) values (4, 'Moscow', 'Moscow', 'Russia', 3);
insert into Address (id, street, city, country, customer_id) values (5, 'Copenhagen', 'Copenhagen', 'Denmark', 3);
insert into Address (id, street, city, country, customer_id) values (6, 'Zurich', 'Zurich', 'Switzerland', 3);
insert into Address (id, street, city, country, customer_id) values (7, 'Roma', 'Roma', 'Italia', 3);

insert into Product (id, name, description, price, retired) values (1, 'iPad', 'Apple tablet device', 499.1, null);
insert into Product (id, name, description, price, retired) values (2, 'MacBook Pro', 'Apple notebook', 1299.5, 0);
insert into Product (id, name, description, price, retired) values (3, 'Dock', 'Dock for iPhone/iPad', 49.4, 1);

insert into Product_Attributes (attributes_key, product_id, attributes) values ('connector', 1, 'socket');
insert into Product_Attributes (attributes_key, product_id, attributes) values ('connector', 3, 'plug');

insert into Orders (id, customer_id, billingaddress_id, shippingaddress_id) values (1, 1, 1, 2);
insert into Orders (id, customer_id, billingaddress_id, shippingaddress_id) values (2, 3, 2, 1);
insert into LineItem (id, product_id, amount, order_id, price) values (1, 1, 2, 1, 499.0);
insert into LineItem (id, product_id, amount, order_id, price) values (2, 2, 1, 1, 1299.0);

insert into DiscountProgram (id, name) values (1, 'Silver Discount Program');
insert into DiscountProgram (id, name) values (2, 'Gold Discount Program');
insert into DiscountProgram (id, name) values (3, 'Platinum Discount Program');

insert into customer_discount (customer_id, discount_program_id) values (1, 3);
insert into customer_discount (customer_id, discount_program_id) values (2, 1);
insert into customer_discount (customer_id, discount_program_id) values (3, 2);

insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (1, 'integer search test', 1234567, 521, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (2, 'decimal search test', 456, 31264, 1499.99);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (3, '#<,&«$''(*@×¢¤₤€¥ª ™®© ØøÅåÆæĈę ¦_{~>½', 789, 62342,  22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (4, 'Case Sensitivity Test', 901, 823, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (5, 'Case sensitivity test', 901, 9521, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (6, 'query partial search test', 234, 9164, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (7, 'partial querysearch test', 345, 612325, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (8, 'search test by partial query', 567, 623412, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (9, 'Id search test', 234, 2932, 21.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (10, 'primitive integer search test', 345, 15235, 22.2);
insert into FilterTestEntity (id, textfield, integerfield, primitiveintegerfield, decimalfield) values (11, 'combined criteria search test', 345, 31264, 61.12);

insert into ParentTestEntity (id, name) values (1, 'Parent Item1');
insert into ParentTestEntity (id, name) values (2, 'Parent Item2');
insert into ParentTestEntity (id, name) values (3, 'Parent Item3');

insert into ChildTestEntity (id, name, parent_id) values (1, 'Child Item 1', 1);
insert into ChildTestEntity (id, name, parent_id) values (2, 'Child Item 2', 2);
insert into ChildTestEntity (id, name, parent_id) values (3, 'Child Item 3', 3);

insert into TestProduct (id, name, price) values (1, 'Product 1', 499.0);
insert into TestProduct (id, name, price) values (2, 'Product 2', 1299.0);
insert into TestProduct (id, name, price) values (3, 'Product 3', 49.0);

insert into TestOrders (id, name) values (1, 'Order1: 100 line items');
insert into TestOrders (id, name) values (2, 'Order2: no line items');
insert into TestOrders (id, name) values (3, 'Order3: 3 line items');

insert into TestCustomer (id, email, firstname, lastname) values (1, 'dave@dmband1.com', 'Dave', 'Matthews1')
insert into TestCustomer (id, email, firstname, lastname) values (2, 'carter@dmband2.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (3, 'boyd@dmband3.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (4, 'dave@dmband4.com', 'Dave', 'Matthews2')
insert into TestCustomer (id, email, firstname, lastname) values (5, 'carter@dmband5.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (6, 'boyd@dmband6.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (7, 'dave@dmband7.com', 'Dave', 'Matthews3')
insert into TestCustomer (id, email, firstname, lastname) values (8, 'carter@dmband8.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (9, 'boyd@dmband9.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (10, 'dave@dmband10.com', 'Dave', 'Matthews4')
insert into TestCustomer (id, email, firstname, lastname) values (11, 'carter@dmband11.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (12, 'boyd@dmband12.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (13, 'dave@dmband13.com', 'Dave', 'Matthews5')
insert into TestCustomer (id, email, firstname, lastname) values (14, 'carter@dmband14.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (15, 'boyd@dmband15.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (16, 'dave@dmband16.com', 'Dave', 'Matthews6')
insert into TestCustomer (id, email, firstname, lastname) values (17, 'carter@dmband17.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (18, 'boyd@dmband18.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (19, 'dave@dmband19.com', 'Dave', 'Matthews7')
insert into TestCustomer (id, email, firstname, lastname) values (20, 'carter@dmband20.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (21, 'boyd@dmband21.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (22, 'dave@dmband22.com', 'Dave', 'Matthews8')
insert into TestCustomer (id, email, firstname, lastname) values (23, 'carter@dmband23.com', 'Carter', 'Beauford');
insert into TestCustomer (id, email, firstname, lastname) values (24, 'boyd@dmband24.com', 'Boyd', 'Tinsley');
insert into TestCustomer (id, email, firstname, lastname) values (25, 'boyd@dmband25.com', 'Boyd', 'Matthews1');
insert into TestCustomer (id, email, firstname, lastname) values (26, 'to@delete.com', 'Test', 'To delete' );
insert into TestCustomer (id, email, firstname, lastname) values (27, 'notTo@delete.com', 'Test', 'Not to delete' );

insert into TestLineItem (id, order_id, product_id, amount) values (1, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (2, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (3, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (4, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (5, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (6, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (7, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (8, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (9, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (10, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (11, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (12, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (13, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (14, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (15, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (16, 1, 1, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (17, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (18, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (19, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (20, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (21, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (22, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (23, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (24, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (25, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (26, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (27, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (28, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (29, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (30, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (31, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (32, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (33, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (34, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (35, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (36, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (37, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (38, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (39, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (40, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (41, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (42, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (43, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (44, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (45, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (46, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (47, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (48, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (49, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (50, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (51, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (52, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (53, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (54, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (55, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (56, 1, 2, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (57, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (58, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (59, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (60, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (61, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (62, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (63, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (64, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (65, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (66, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (67, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (68, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (69, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (70, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (71, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (72, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (73, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (74, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (75, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (76, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (77, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (78, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (79, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (80, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (81, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (82, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (83, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (84, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (85, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (86, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (87, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (88, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (89, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (90, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (91, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (92, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (93, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (94, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (95, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (96, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (97, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (98, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (99, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (100, 1, 3, 1);
insert into TestLineItem (id, order_id, product_id, amount) values (101, 3, 1, 12);
insert into TestLineItem (id, order_id, product_id, amount) values (102, 3, 2, 130);
insert into TestLineItem (id, order_id, product_id, amount) values (103, 3, 3, 1050);
