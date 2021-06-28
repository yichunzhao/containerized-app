insert into Client (id, name, email) values(1, 'ynz','ynz@hotmail.com');

insert into Client_orders(id, creation_date_time_with_zone, order_status, FK_Client_Id) values(2, '1999-01-08 14:05:06+01', 'SUSPENDING',1);
insert into Client_orders(id, creation_date_time_with_zone, order_status, FK_Client_Id) values(3, '2000-02-18 14:05:06+01', 'SUSPENDING',1);

insert into Order_Items(id, product_name, amount, FK_Order_Id) values(5, 'iphone11' ,1, 2);
insert into Order_Items(id, product_Name, amount, FK_Order_Id) values(6, 'Logitech MK270' ,1, 2);
insert into Order_Items(id, product_Name, amount, FK_Order_Id) values(7, 'B&O HeadPhone' ,1, 3);