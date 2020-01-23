-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 23, 2020 at 01:57 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hotel`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `payment_id` int(11) NOT NULL,
  `price` int(11) DEFAULT NULL,
  `checkin_date` date NOT NULL,
  `checkout_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id`, `room_id`, `payment_id`, `price`, `checkin_date`, `checkout_date`) VALUES
(28, 202, 1, NULL, '2020-01-23', NULL),
(29, 402, 2, NULL, '2020-01-23', NULL),
(30, 303, 3, 110, '2020-01-23', '2020-01-23'),
(31, 305, 4, NULL, '2020-01-23', NULL),
(32, 102, 4, NULL, '2020-01-23', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

CREATE TABLE `guest` (
  `id` int(11) NOT NULL,
  `first_name` varchar(55) NOT NULL,
  `last_name` varchar(55) NOT NULL,
  `email` varchar(55) NOT NULL,
  `address` varchar(255) NOT NULL,
  `doc_number` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`id`, `first_name`, `last_name`, `email`, `address`, `doc_number`) VALUES
(0, 'Mary', 'Ryan', 'mary.ry@gmx.at', 'Spittelwiese 23, 8692 Veitschbach', '98674367'),
(1, 'Flora', 'Clarke', 'fk@gail.com', 'Ernstbrunner Straße 74, 4462 Reichraming', '16938406'),
(2, 'Martin', 'Todd', 'm.todd88@gmx.at', 'Hauptplatz 39, 4132 Oberlembach', '29583750'),
(3, 'Sherill', 'Poston', 'sherill.poston@gmx.at', 'Glocknerstrasse 20, 8584 Hirschegg-Piber-Sonnseite', '12345678'),
(4, 'Jack', 'Rodgers', 'jr45@gmx.at', 'Lettental 78, 9772 Suppersberg', '72837200'),
(5, 'Wally', 'Leger', 'wally.leger@outlook.com', 'Waldstrasse 40, 9544 Carinthia', '24149767'),
(6, 'Carina', 'Theo', 'car.theo@gmx.at', 'Prager Strasse 96, 7222 Rohrbach bei Mattersburg', '29184967'),
(7, 'Wanda', 'Kelly', 'wanda.kelly@yahoo.at', 'Horner Strasse 71m 4090 Saag', '49204536'),
(8, 'Fred', 'Moore', 'fred1234@gmx.at', 'Aspernstrasse 23, 9462 Schiefling', '12049583'),
(9, 'Rose', 'Davis', 'rose.davis@gmx.at', 'Floridusgasse 52, 1130 Wien', '33557535'),
(10, 'Earl', 'Hall', 'earlhall938@gmail.com', 'Waldstrasse 48, 4030 Felling', '18190191'),
(11, 'Ann', 'Perez', 'annperez1988@gmx.at', 'Rossmarkt 64, 1210 Langenzersdorf', '39483911'),
(12, 'Marlin', 'Estes', 'marli.estes@gmx.at', 'Steinamangerer Strasse 21, 4342 Pitzing', '241894967'),
(13, 'Joe', 'Doe', 'joe.doe@mail.com', 'none', '12343256'),
(14, 'Jane', 'Doe', 'jane.doe@mail.com', 'right here', '987654');

-- --------------------------------------------------------

--
-- Table structure for table `guest_booking`
--

CREATE TABLE `guest_booking` (
  `booking_id` int(11) NOT NULL,
  `guest_id` int(11) NOT NULL,
  `iscontactperson` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `guest_booking`
--

INSERT INTO `guest_booking` (`booking_id`, `guest_id`, `iscontactperson`) VALUES
(28, 1, 0),
(28, 13, 1),
(29, 9, 1),
(30, 5, 1),
(31, 11, 1),
(31, 12, 0),
(32, 7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `id` int(11) NOT NULL,
  `payment_method_id` int(11) NOT NULL,
  `payment_status_id` int(11) NOT NULL,
  `payment_system_id` varchar(55) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`id`, `payment_method_id`, `payment_status_id`, `payment_system_id`, `amount`) VALUES
(1, 3, 1, 'BK123EX987', 0),
(2, 1, 1, 'BK123EX999', 0),
(3, 4, 0, 'BK123EX911', 110),
(4, 1, 1, 'ABC123456Z', 0),
(5, 4, 1, 'XYZ12234DF', 0);

-- --------------------------------------------------------

--
-- Table structure for table `payment_method`
--

CREATE TABLE `payment_method` (
  `id` int(11) NOT NULL,
  `name` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_method`
--

INSERT INTO `payment_method` (`id`, `name`) VALUES
(0, 'Paypal'),
(1, 'Master Card'),
(2, 'VISA Card'),
(3, 'Direct Transfer'),
(4, 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `payment_status`
--

CREATE TABLE `payment_status` (
  `id` int(11) NOT NULL,
  `name` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_status`
--

INSERT INTO `payment_status` (`id`, `name`) VALUES
(0, 'paid'),
(1, 'open');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `room_number` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `size` int(11) NOT NULL,
  `isAvailable` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_number`, `type_id`, `size`, `isAvailable`) VALUES
(101, 0, 15, 1),
(102, 0, 14, 1),
(103, 1, 22, 0),
(104, 1, 22, 0),
(105, 2, 35, 0),
(201, 3, 56, 1),
(202, 3, 55, 0),
(203, 4, 30, 1),
(204, 4, 30, 1),
(205, 1, 27, 1),
(301, 5, 90, 0),
(302, 2, 23, 1),
(303, 0, 14, 1),
(304, 0, 15, 1),
(305, 1, 21, 0),
(401, 2, 35, 1),
(402, 2, 36, 0),
(403, 3, 55, 1),
(404, 4, 30, 1),
(501, 3, 80, 1),
(502, 3, 95, 1);

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `id` int(11) NOT NULL,
  `name` varchar(55) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`id`, `name`, `price`) VALUES
(0, 'Breakfast', 15),
(1, 'WIFI', 20),
(2, 'Extra Bed', 10);

-- --------------------------------------------------------

--
-- Table structure for table `type`
--

CREATE TABLE `type` (
  `id` int(11) NOT NULL,
  `capacity` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `equipment` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `type`
--

INSERT INTO `type` (`id`, `capacity`, `price`, `equipment`, `description`) VALUES
(0, 1, 90, 'TV, Coffee Machine, Shower', 'Room for One'),
(1, 2, 140, 'TV, Coffee Machine, Shower', 'Room for Two'),
(2, 2, 228, 'TV, DVD-Player, Coffee Machine, Shower, Bathtub', 'Deluxe Room for Two'),
(3, 4, 818, 'DVD-Player, TV, Coffee Machine, Shower, Bathtub', 'Deluxe Suite for Four'),
(4, 6, 356, 'TV', 'Budget Group Room for Six'),
(5, 8, 1601, 'TV, DVD-Player, Coffee Machine, Shower, Bathtub', 'Deluxe Royal Suite for Eight');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payment_id` (`payment_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Indexes for table `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guest_booking`
--
ALTER TABLE `guest_booking`
  ADD PRIMARY KEY (`booking_id`,`guest_id`),
  ADD KEY `guest_booking_ibfk_2` (`guest_id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payment_status_id` (`payment_status_id`),
  ADD KEY `payment_method_id` (`payment_method_id`);

--
-- Indexes for table `payment_method`
--
ALTER TABLE `payment_method`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payment_status`
--
ALTER TABLE `payment_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`room_number`),
  ADD KEY `type_id` (`type_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `type`
--
ALTER TABLE `type`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `payment_status`
--
ALTER TABLE `payment_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `type`
--
ALTER TABLE `type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`),
  ADD CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`id`);

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
