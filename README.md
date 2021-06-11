
This is a Spring Boot application written in java. The application is divided to three layers:
The first layer is a REST API architecturally designed code in charge of communicating with the client side- POST, GET, DELETE exc.
The second layer is the service layer where data is calculated and manipulated for whatever is needed.
The third layer uses JPA repository, HQL and some native SQL in order to do all CRUD operations on data which is stored in a MySQL data base.

Basically, there are three types of users: an ADMIN which in our case is hard coded (but can easilly be changed to an @Entity), a COMPANY and a CUSTOMER.

The ADMIN is like the queen on a chess board, he can to all the operations that can be doen by the COMPANY and CUSTOMER and more.

A COMPANY can update its own fields and more importantly- create update and delete coupons.

A CUSTOMER can also update his own fields and buy or cancle a purchase of said coupons.

The design:

  COPMANY    ->    OneToMany    ->     COUPON
  
  CUSTOMER   ->    ManyToMany   ->     COUPON --> using a joined table called - customer_coupon with a constraint that a customer can't buy the same coupon twice. 
