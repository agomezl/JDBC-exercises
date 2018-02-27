# Exercise 1

Write the following embedded SQL queries, based on the database schema:

```
Product(maker, model, type)
PC(model, speed, ram, hd, price)
Laptop(model, speed, ram, hd, screen, price)
Printer(model, color, type, price)
```

* (**A**) Ask the user for a price and find the PC whose price is
  closest to the desired price. Print the maker, model number, and
  speed of the PC.

* (**B**) Ask the user for minimum values of the speed, RAM, hard-disk size,
  and screen size that they will accept. Find all the laptops that
  satisfy these requirements. Print their specifications (all
  attributes of Laptop) and their manufacturer.

* (**C**) Ask the user for a manufacturer. Print the specifications of all
  products by that manufacturer. That is, print the model number,
  product-type, and all the attributes of whichever relation is
  appropriate for that type.

* (**D**) Ask the user for a “budget” (total price of a PC and
  printer), and a minimum speed of the PC. Find the cheapest “system”
  (PC plus printer) that is within the budget and minimum speed, but
  make the printer a color printer if possible. Print the model
  numbers for the chosen system.

* (**E**) Ask the user for a manufacturer, model number, speed, RAM,
  hard-disk size, and price of a new PC. Check that there is no PC
  with that model number. Print a warning if so, and otherwise insert
  the information into tables Product and PC.
