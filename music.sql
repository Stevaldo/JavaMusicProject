-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 16, 2023 at 05:19 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `music`
--

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE `country` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`source`, `title`, `banner`) VALUES
('renungkan.wav', 'Untuk kita renungkan - Ebiet. G Ade', 'untukkita.jpg'),
('bento.wav', 'Bento - Iwan Fals', 'bento.jpg'),
('takemehome.wav', 'Take me home,Country Roads - John Denver', 'countryRoads.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `edm`
--

CREATE TABLE `edm` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `edm`
--

INSERT INTO `edm` (`source`, `title`, `banner`) VALUES
('onmyway.wav', 'On my way - Alan Walker', 'onmyway.jpg'),
('happier.wav', 'Happier - Marshmello', 'happier.jpg'),
('waybackhome.wav', 'Way back home - Shaun', 'waybackhome.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `gospel`
--

CREATE TABLE `gospel` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gospel`
--

INSERT INTO `gospel` (`source`, `title`, `banner`) VALUES
('wearethereason.wav', 'We are the Reason - Avalon', 'avalon.jpg'),
('oholynight.wav', 'O Holy Night - Mariah Carey', 'oholynightnew.jpeg'),
('ramadhan.wav', 'Ramadhan Tiba - Opick', 'opicknew.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `jazz`
--

CREATE TABLE `jazz` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jazz`
--

INSERT INTO `jazz` (`source`, `title`, `banner`) VALUES
('flymetothemoon.wav', 'Fly Me To The Moon - 2008 Remastered', 'Flyme.jpg'),
('herewegoagain.wav', 'Here we go again - Ardhito Pramono', 'ardhito.jpeg'),
('justthetwoofus.wav', 'Just the two of us - Bill withers', 'JustThe.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `username` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `email` varchar(50) NOT NULL,
  `nohp` varchar(50) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `premium` tinyint(1) NOT NULL,
  `premiumRequest` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`username`, `password`, `email`, `nohp`, `active`, `premium`, `premiumRequest`) VALUES
('hey', 'tayo', 'hey', 'tayo', 0, 1, 0),
('zerft', '123', 'stevaldoclaudio', '087780620099', 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pop`
--

CREATE TABLE `pop` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pop`
--

INSERT INTO `pop` (`source`, `title`, `banner`) VALUES
('rapsodi.wav', 'Rapsodi - JKT48', 'zeejkt48.jpeg'),
('perfect.wav', 'Perfect - Ed Sheeran', 'perfect.jpg'),
('kirari.wav', 'Kirari - Fujii Kaze', 'fujikaze.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `rock`
--

CREATE TABLE `rock` (
  `source` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `banner` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rock`
--

INSERT INTO `rock` (`source`, `title`, `banner`) VALUES
('always.wav', 'Always - Bon Jovi', 'bonjovi.jpg'),
('enter.wav', 'Enter Sandman - Metalica', 'Metalica.jpg'),
('sweetchild.wav', 'Sweet Child O Mine - GunNroses', 'gunnroses.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
