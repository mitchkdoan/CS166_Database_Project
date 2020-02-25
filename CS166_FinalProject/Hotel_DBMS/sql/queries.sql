INSERT INTO Customer
VALUES (customerID, fName, lName, address, phNo, DOB, gender);

INSERT INTO Room
VALUES (hotelID, roomNo, roomType);

INSERT INTO MaintenanceCompany
VALUES (cmpID, name, address, isCertified);

INSERT INTO Repair
VALUES (rID, hotelID, roomNo, mCompany, repairDate, description, repairType);

INSERT INTO Booking
VALUES (bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price);

UPDATE Assigned
SET roomNo = roomNoVar
WHERE hotelID = hotelIDVar and staffID = staffIDVar;

string managerVar = "SELECT h.manager FROM Hotel h WHERE h.hID = hIDVar";
INSERT INTO Request
VALUES (reqIDrepairID, managerVar, repairID, requestDate, description);

SELECT Total_Rooms - Booked_Rooms as Available_Rooms
FROM ((SELECT r.roomNo as Total_Rooms
	FROM Room r
	WHERE r.hotelID = hotelID) AND
	(SELECT r.roomNo as Booked_Rooms
	  FROM Room r, Booking b
	  WHERE r.hotelID = b.hotelID))

SELECT COUNT(*)
FROM Booking b, Room r
WHERE b.hotelID = r.hotelID AND b.roomNo = r.roomNo;

SELECT b.roomNo, b.roomType. b.price
FROM Booking b
LIMIT K
ORDER BY b.price ASC;

SELECT r.repairType, r.hotelID, r.roomNo
FROM Repair r, MaintenanceCompany m
WHERE r.mCompany = m.cmpID AND m.name = name;

SELECT COUNT(*)
FROM Repair r
WHERE r.roomNo = givenRoomNo AND r.hotelID = givenHotelID AND r.repairDate BETWEEN r.repairDate AND DATEADD(month, 12, r.repairDate);

// MITCHELL
SELECT r.roomNo
FROM Booking b, Room r
WHERE b.hotelID = r.hotelID AND b.roomNo = r.roomNo AND b.bookingDate BETWEEN b.bookingDate AND DATEADD(day, 6, b.bookingDate);

SELECT b.price
FROM Booking b, Customer c
WHERE c.customerID = b.customer AND c.fName = fNameVar AND c.lname = lNameVar
LIMIT K
ORDER BY price desc;

DateRange *
SELECT b.price
FROM Booking b, Customer c
WHERE c.customerID = b.customer AND c.fName = fNameVar AND c.lname = lNameVar AND b.hotelID = hotelIDVar;

// List Top K Maintenance Company Names based on total repair count (descending order)
SELECT m.name
FROM MaintenanceCompany m, Repair r
WHERE m.cmpID = r.mCompany
LIMIT K
ORDER BY (SELECT count (*)
FROM MaintenanceCompany m, Repair r
WHERE m.cmpID = r.mCompany) DESC;




