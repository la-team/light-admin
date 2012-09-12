CREATE TABLE customer (
  id BIGINT IDENTITY PRIMARY KEY,
  firstname VARCHAR(255),
  lastname VARCHAR(255),
  email VARCHAR(255));
CREATE UNIQUE INDEX ix_customer_email ON CUSTOMER (email ASC);

CREATE TABLE address (
  id BIGINT IDENTITY PRIMARY KEY,
  customer_id BIGINT CONSTRAINT address_customer_ref REFERENCES customer (id),
  street VARCHAR(255),
  city VARCHAR(255),
  country VARCHAR(255));

CREATE TABLE product (
  id BIGINT IDENTITY PRIMARY KEY,
  name VARCHAR(255),
  description VARCHAR(255),
  price DECIMAL(8,2));

CREATE TABLE product_attributes (
  attributes_key VARCHAR(255),
  product_id BIGINT CONSTRAINT product_attributes_product_ref REFERENCES product (id),
  attributes VARCHAR(255));

CREATE TABLE orders (
  id BIGINT IDENTITY PRIMARY KEY,
  customer_id BIGINT CONSTRAINT orders_customer_ref REFERENCES customer (id),
  billingaddress_id BIGINT CONSTRAINT orders_billingaddress_ref REFERENCES address (id),
  shippingaddress_id BIGINT CONSTRAINT orders_shippingaddress_ref REFERENCES address (id));

CREATE TABLE lineitem (
  id BIGINT IDENTITY PRIMARY KEY,
  product_id BIGINT CONSTRAINT lineitem_product_ref REFERENCES product (id),
  order_id BIGINT CONSTRAINT lineitem_orders_ref REFERENCES orders (id),
  amount BIGINT,
  price DECIMAL(8,2));