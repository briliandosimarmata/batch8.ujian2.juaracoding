-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 22, 2021 at 04:47 AM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `batch8_ujian2`
--

-- --------------------------------------------------------

--
-- Table structure for table `ulangbulan`
--

CREATE TABLE `ulangbulan` (
  `nama` varchar(30) DEFAULT NULL,
  `dateFrom` datetime DEFAULT NULL,
  `platfon` int(13) DEFAULT NULL,
  `bunga` double DEFAULT NULL,
  `lamapinjaman` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ulangbulan`
--

INSERT INTO `ulangbulan` (`nama`, `dateFrom`, `platfon`, `bunga`, `lamapinjaman`) VALUES
('Budi', '2022-09-22 00:00:00', 20000000, 1.2, 15);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
