drop table workHours;
drop table deliveries;
drop table deliveryPersons;
drop table trans_d;
drop table trans_p;
drop table transactions;
drop table happyPrices;
drop table drinks;
drop table ingredients;
drop table payments;
drop table preferences;
drop table pizzas;
drop table prices;
drop table customers;
drop sequence seq_trans;
drop sequence seq_pizzas;

create table customers(email char(25) primary key, 
						password char(100), 
						first_name char(25), 
						last_name char(25), 
						address char(100), 
						total_spent number default 0.0, 
						active char default 'Y');
create table prices(item_id integer,
					type char,
					reg_price number,
					primary key (item_id, type));						
create table pizzas(pizza_id integer primary key,
				    pizza_size char,
				    crust char(10));
create table preferences(customer_email char(25) references customers(email), 
						 pizza_id integer references pizzas(pizza_id),
						 primary key (customer_email, pizza_id));
create table payments(payment_id integer primary key, 
					 customer_email char(25) references customers(email), 
					 type char(10), 
					 card_number integer);
create table ingredients(pizza_id integer references pizzas(pizza_id),
					     name char(15),
					    primary key (pizza_id, name));
create table drinks(drink_id integer primary key,
				   description char(50),
				   manufacturer char(50),
				   supplier char (50));
create table happyPrices(item_id integer,
					    type char,
						day char(2),
						start_time timestamp,
						end_time timestamp,
						price number,
						primary key (item_id, type, day));	
create table transactions(trans_id integer primary key, 
						  payment_id integer references payments(payment_id),
						  email char(25),
						  trans_date date, 
						  status char default 'U',
						  total number, 
						  time timestamp, 
						  eta integer);	
create table trans_p(trans_id integer references transactions(trans_id), 
					 pizza_id integer references pizzas(pizza_id), 
					 qty integer, 
					 primary key (trans_id, pizza_id));				
create table trans_d(trans_id integer references transactions(trans_id),
				     drink_id integer references drinks(drink_id),
					 qty integer,
					 primary key (trans_id, drink_id));
create table deliveryPersons(deliv_id integer primary key,
							password char(100),
							name char(40),
							address char(100),
							vehicle char(25),
							trans_type char,
							cost_mile number,
							curr_loc char(100),
							salary number);			
create table deliveries(trans_id integer primary key references transactions(trans_id),
					    deliv_id integer references deliveryPersons(deliv_id),
					    completed char);

create table workHours(deliv_id integer references deliveryPersons(deliv_id),
					   day char(2),
					   start_hour timestamp,
					   end_hour timestamp,
					   primary key (deliv_id, day));
					
CREATE SEQUENCE seq_trans
	MINVALUE 1
	START WITH 1
	INCREMENT BY 1
	CACHE 10;
CREATE SEQUENCE seq_pizzas
	MINVALUE 1
	START WITH 1
	INCREMENT BY 1
	CACHE 10;

insert into drinks values 	(1, 'coke', 'Coca-Cola', 'Coke');
insert into drinks values 	(2, 'less calories in this coke', 'Coca-Cola', 'Diet Coke');
insert into drinks values 	(3, 'a citrus soda.', 'Coca-Cola', 'Mello Yellow');
insert into drinks values 	(4, 'a lemon lime soda', 'Coca-Cola', 'Sprite');
insert into drinks values 	(5, 'Dr. Pepper', 'Coca-Cola', 'Dr. Pepper');
insert into drinks values 	(6, 'a bottle of water', 'Coca-Cola', 'Dasani Water');
insert into drinks values 	(7, 'A light mexican beer.', 'Grupo Modelo', 'Corona');
insert into drinks values 	(8, 'Bud Light', 'Anheuser-Busch', 'Bud Light');
insert into drinks values 	(9, 'Budweiser', 'Anheuser-Busch', 'Budweiser');
insert into drinks values 	(10, 'An Italian Beer', 'SAB Miller', 'Peroni');
insert into drinks values 	(11, 'Yuengling Lager', 'D.G. Yuengling', 'Yuengling');

insert into prices (item_id, type, reg_price) values (1, 'D', 3.00);
insert into prices (item_id, type, reg_price) values (2, 'D', 3.00);
insert into prices (item_id, type, reg_price) values (3, 'D', 3.00);
insert into prices (item_id, type, reg_price) values (4, 'D', 3.00);
insert into prices (item_id, type, reg_price) values (5, 'D', 3.00);
insert into prices (item_id, type, reg_price) values (6, 'D', 2.50);
insert into prices (item_id, type, reg_price) values (7, 'D', 5.00);
insert into prices (item_id, type, reg_price) values (8, 'D', 5.00);
insert into prices (item_id, type, reg_price) values (9, 'D', 5.00);
insert into prices (item_id, type, reg_price) values (10, 'D', 5.00);
insert into prices (item_id, type, reg_price) values (11, 'D', 5.00);


					
