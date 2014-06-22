--
-- Sample dataset containing a number of Hotels in various Cities across the world.
--

-- =================================================================================================
-- AUSTRALIA

-- Brisbane
insert into city(country, name, state, map) values ('Australia', 'Brisbane', 'Queensland', '-27.470933, 153.023502')
insert into hotel(city_id, name, address, zip) values (1, 'Conrad Treasury Place', 'William & George Streets', '4001')

-- Melbourne
insert into city(country, name, state, map) values ('Australia', 'Melbourne', 'Victoria', '-37.813187, 144.96298')
insert into hotel(city_id, name, address, zip) values (2, 'The Langham', '1 Southgate Ave, Southbank', '3006')

-- Sydney
insert into city(country, name, state, map) values ('Australia', 'Sydney', 'New South Wales', '-33.868901, 151.207091')
insert into hotel(city_id, name, address, zip) values (3, 'Swissotel', '68 Market Street', '2000')


-- =================================================================================================
-- CANADA

-- Montreal
insert into city(country, name, state, map) values ('Canada', 'Montreal', 'Quebec', '45.508889, -73.554167')
insert into hotel(city_id, name, address, zip) values (4, 'Ritz Carlton', '1228 Sherbrooke St', 'H3G1H6')


-- =================================================================================================
-- ISRAEL

-- Tel Aviv
insert into city(country, name, state, map) values ('Israel', 'Tel Aviv', '', '32.066157, 34.777821')
insert into hotel(city_id, name, address, zip) values (5, 'Hilton Tel Aviv', 'Independence Park', '63405')


-- =================================================================================================
-- JAPAN

-- Tokyo
insert into city(country, name, state, map) values ('Japan', 'Tokyo', '', '35.689488, 139.691706')
insert into hotel(city_id, name, address, zip) values (6, 'InterContinental Tokyo Bay', 'Takeshiba Pier', '105')


-- =================================================================================================
-- SPAIN

-- Barcelona
insert into city(country, name, state, map) values ('Spain', 'Barcelona', 'Catalunya', '41.387917, 2.169919')
insert into hotel(city_id, name, address, zip) values (7, 'Hilton Diagonal Mar', 'Passeig del Taulat 262-264', '08019')

-- =================================================================================================
-- SWITZERLAND

-- Neuchatel
insert into city(country, name, state, map) values ('Switzerland', 'Neuchatel', '', '46.992979, 6.931933')
insert into hotel(city_id, name, address, zip) values (8, 'Hotel Beaulac', ' Esplanade Leopold-Robert 2', '2000')


-- =================================================================================================
-- UNITED KINGDOM

-- Bath
insert into city(country, name, state, map) values ('UK', 'Bath', 'Somerset', '51.381428, -2.357454')
insert into hotel(city_id, name, address, zip) values (9, 'The Bath Priory Hotel', 'Weston Road', 'BA1 2XT')
