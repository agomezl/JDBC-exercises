# Exercise 2

Write the following embedded SQL queries, based on the database schema:

```
Classes(class, type, country, numGuns, bore, displacement)
Ships(name, class, launched)
Battles(name, date)
Outcomes(ship, battle, result)
```

* (**A**) The firepower of a ship is roughly proportional to the number of
  guns times the cube of the bore of the guns. Find the class with
  the largest firepower.

* (**B**) Ask the user for the name of a battle. Find the countries of the
  ships involved in the battle. Print the country with the most
  ships sunk and the country with the most ships damaged.

* (**C**) Ask the user for the name of a class and the other information
  required for a tuple of table Classes. Then ask for a list of
  the names of the ships of that class and their dates launched.
  However, the user need not give the first name, which will be the
  name of the class. Insert the information gathered into Classes
  and Ships.
