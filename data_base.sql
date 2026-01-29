-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: ticket_management_database
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS events;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  event_id int NOT NULL AUTO_INCREMENT,
  title varchar(45) NOT NULL,
  category varchar(45) DEFAULT NULL,
  `date` varchar(45) NOT NULL,
  location varchar(45) DEFAULT NULL,
  capacity int NOT NULL,
  seats_available int NOT NULL,
  PRIMARY KEY (event_id)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES events WRITE;
/*!40000 ALTER TABLE events DISABLE KEYS */;
INSERT INTO events VALUES (1,'Tech Conference 2025','Conference','2025-12-15 09:00','Riyadh',50,40),(2,'Web Development Workshop','Workshop','2025-11-30 14:00','Jeddah',10,0),(3,'Winter Carnival','Entertainment','2025-12-26 18:00','Qassim',1000,991),(4,'Football Tournament','Sports','2025-12-06 16:00','Jeddah',500,490),(5,'Art Exhibition','Entertainment','2026-01-01 10:00','Dammam',3,0);
/*!40000 ALTER TABLE events ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrations`
--

DROP TABLE IF EXISTS registrations;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE registrations (
  reg_id int NOT NULL AUTO_INCREMENT,
  user_id int NOT NULL,
  event_id int NOT NULL,
  PRIMARY KEY (reg_id),
  KEY user_id_idx (user_id),
  KEY event_id_idx (event_id),
  CONSTRAINT fk_registrations_event FOREIGN KEY (event_id) REFERENCES `events` (event_id),
  CONSTRAINT fk_registrations_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrations`
--

LOCK TABLES registrations WRITE;
/*!40000 ALTER TABLE registrations DISABLE KEYS */;
INSERT INTO registrations VALUES (1,3,1),(2,3,3),(3,3,2),(4,3,4),(5,4,1),(6,4,2),(7,4,4),(8,5,1),(9,5,3),(10,5,4),(11,6,1),(12,6,2),(13,6,3),(14,6,4),(15,7,2),(16,7,4),(17,8,4),(18,8,2),(19,8,1),(20,8,3),(21,9,1),(22,9,2),(23,9,3),(24,9,4),(25,10,1),(26,10,2),(27,10,3),(28,10,4),(29,11,1),(30,11,2),(31,11,3),(32,11,4),(33,14,1),(34,14,2),(35,14,3),(36,14,4),(37,3,5),(38,8,5),(39,5,5),(40,16,1),(41,16,2),(42,16,3);
/*!40000 ALTER TABLE registrations ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS tickets;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE tickets (
  ticket_id int NOT NULL AUTO_INCREMENT,
  user_id int NOT NULL,
  event_id int NOT NULL,
  ticket_unique_id varchar(50) DEFAULT NULL,
  ticket_status varchar(20) DEFAULT 'ACTIVE',
  qr_code_data text,
  PRIMARY KEY (ticket_id),
  UNIQUE KEY ticket_unique_id (ticket_unique_id),
  KEY user_id_idx (user_id),
  KEY event_id_idx (event_id),
  CONSTRAINT fk_tickets_event FOREIGN KEY (event_id) REFERENCES `events` (event_id),
  CONSTRAINT fk_tickets_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES tickets WRITE;
/*!40000 ALTER TABLE tickets DISABLE KEYS */;
INSERT INTO tickets VALUES (1,3,1,'TKT-9606BABB','ACTIVE','QR_DATA_TKT-9606BABB'),(2,3,3,'TKT-CAC1761F','ACTIVE','QR_DATA_TKT-CAC1761F'),(3,3,2,'TKT-9E5A0332','ACTIVE','QR_DATA_TKT-9E5A0332'),(4,3,4,'TKT-DF24BE0F','ACTIVE','QR_DATA_TKT-DF24BE0F'),(5,4,1,'TKT-2DA7FC36','ACTIVE','QR_DATA_TKT-2DA7FC36'),(6,4,2,'TKT-9EFA9AC9','ACTIVE','QR_DATA_TKT-9EFA9AC9'),(7,4,4,'TKT-BE5716A9','ACTIVE','QR_DATA_TKT-BE5716A9'),(8,5,1,'TKT-41CFA541','ACTIVE','QR_DATA_TKT-41CFA541'),(9,5,3,'TKT-014D7169','ACTIVE','QR_DATA_TKT-014D7169'),(10,5,4,'TKT-B7DCA1B6','ACTIVE','QR_DATA_TKT-B7DCA1B6'),(11,6,1,'TKT-2A987E28','ACTIVE','QR_DATA_TKT-2A987E28'),(12,6,2,'TKT-3BA9BE05','ACTIVE','QR_DATA_TKT-3BA9BE05'),(13,6,3,'TKT-BF4EF289','ACTIVE','QR_DATA_TKT-BF4EF289'),(14,6,4,'TKT-FC807245','ACTIVE','QR_DATA_TKT-FC807245'),(15,7,2,'TKT-0A781F06','ACTIVE','QR_DATA_TKT-0A781F06'),(16,7,4,'TKT-2AFFBEA4','ACTIVE','QR_DATA_TKT-2AFFBEA4'),(17,8,4,'TKT-974C0059','ACTIVE','QR_DATA_TKT-974C0059'),(18,8,2,'TKT-C4BEB9DD','ACTIVE','QR_DATA_TKT-C4BEB9DD'),(19,8,1,'TKT-3D6F7E04','ACTIVE','QR_DATA_TKT-3D6F7E04'),(20,8,3,'TKT-F8EEEF6A','ACTIVE','QR_DATA_TKT-F8EEEF6A'),(21,9,1,'TKT-A2F4CC4B','ACTIVE','QR_DATA_TKT-A2F4CC4B'),(22,9,2,'TKT-D7088EAE','ACTIVE','QR_DATA_TKT-D7088EAE'),(23,9,3,'TKT-5B383CB4','ACTIVE','QR_DATA_TKT-5B383CB4'),(24,9,4,'TKT-2024A87E','ACTIVE','QR_DATA_TKT-2024A87E'),(25,10,1,'TKT-620C028D','ACTIVE','QR_DATA_TKT-620C028D'),(26,10,2,'TKT-C13C0BA9','ACTIVE','QR_DATA_TKT-C13C0BA9'),(27,10,3,'TKT-180F5C33','ACTIVE','QR_DATA_TKT-180F5C33'),(28,10,4,'TKT-7AD02A6B','ACTIVE','QR_DATA_TKT-7AD02A6B'),(29,11,1,'TKT-2BD19679','ACTIVE','QR_DATA_TKT-2BD19679'),(30,11,2,'TKT-0209BB84','ACTIVE','QR_DATA_TKT-0209BB84'),(31,11,3,'TKT-168B95E8','ACTIVE','QR_DATA_TKT-168B95E8'),(32,11,4,'TKT-6B2D8D2D','ACTIVE','QR_DATA_TKT-6B2D8D2D'),(33,14,1,'TKT-72735BDC','ACTIVE','QR_DATA_TKT-72735BDC'),(34,14,2,'TKT-2CF28AEB','ACTIVE','QR_DATA_TKT-2CF28AEB'),(35,14,3,'TKT-B8DB4B80','ACTIVE','QR_DATA_TKT-B8DB4B80'),(36,14,4,'TKT-17A93C31','ACTIVE','QR_DATA_TKT-17A93C31'),(37,3,5,'TKT-921833FE','ACTIVE','QR_DATA_TKT-921833FE'),(38,8,5,'TKT-7451DC71','ACTIVE','QR_DATA_TKT-7451DC71'),(39,5,5,'TKT-C95B79AB','ACTIVE','QR_DATA_TKT-C95B79AB'),(40,16,1,'TKT-58C6E0B9','ACTIVE','QR_DATA_TKT-58C6E0B9'),(41,16,2,'TKT-BA10B5A8','ACTIVE','QR_DATA_TKT-BA10B5A8');
/*!40000 ALTER TABLE tickets ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS users;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE users (
  user_id int NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` enum('admin','organizer','attendee','new_option') NOT NULL,
  password_salt varchar(255) NOT NULL DEFAULT 'default_salt',
  PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES users WRITE;
/*!40000 ALTER TABLE users DISABLE KEYS */;
INSERT INTO users VALUES (1,'admin','BGvg66OUTfoluyEItHq2/1dGqd+psa/nMmoj9rpU+qw=','admin','ryHZIPParpNr0f1yNTtkCw=='),(2,'organizer1','Q+vzueg1NpbmuX5eDoyzUVN47HTKwaiDyJuRABfPkQQ=','organizer','7OLlVy6gl9QhQzmfg817aQ=='),(3,'attendee1','TCYzcY5uRxVQUA+jzcsZJ2w+ti58xUqGufaJt7ugQTQ=','attendee','PKy4nh1I6SIAqNC63t+bLA=='),(4,'attendee2','dXGga6haFKDS18+Qg4UEEuxNK0rXI0RhVKdZihHvpnA=','attendee','SQn17QViXXpaSMPfeTxG+Q=='),(5,'attendee3','/5azDSjjS5zQ7jOfwC6hQoVaCLwZC6BjRZnDUpP4o4o=','attendee','En8YIBIg3rYeB0Hd8lk4mw=='),(6,'attendee4','wJv/mvR28YX2VUwLsZ+70FzroygIylfhWQv6zkIfgzk=','attendee','M6DQcKK6I6ATRY2GZ4QQtA=='),(7,'attendee5','nRijQipGNcfWlOypczZKpXT93u/PhuBS78o1QNVjpZ0=','attendee','pbacexAFDNs7xpjCCAF1LA=='),(8,'attendee6','ezsUqqRPJgRvHlTkzhRcIJd+1Y4o3LO7hUHeDgCP0ZA=','attendee','fIwz1gcRvupxDAW7GXaJnQ=='),(9,'attendee7','aVHIm5RaXTV0TCkAQ04ehK07hCgtAF5q1x3Lacd4Ma4=','attendee','1z0mBRIAGviJ7HosaD0ZRw=='),(10,'attendee8','Sway0/+xHcKRDT4n4VV0+t5BGLofKDZsh6H4kOFSiTY=','attendee','IksY0map7t3Sr2U2jQ62rA=='),(11,'attendee9','OE7agbHtlwjsTBjQHhThjPO3s4CbySgpKWlJbX8acHQ=','attendee','3MdrMf8sLQZXD4PLED2rJA=='),(12,'organizer2','V4BTJ3W/oWL6J98pKZeZ++aCUYDkpDf+qyctnfwPM30=','organizer','x4XmJjBAH9WXdZ6VsSLQLQ=='),(13,'eventmaster','SgY4N27Z4WTkgEubyxzQWg6zK7HxsPS+o+c76RgcPY8=','organizer','sya/IyNKHB2cVGbi5P1h/Q=='),(14,'attendee10','gRPzC/fHqsSRZMRu/j2/A92tQ3IUcpK85mYaSQSl7lg=','attendee','qmQUgO1oac8TwBDSFu20oQ=='),(15,'superadmin','wWRcamsbcYO+Z3o2teTQm8JvOYvPv1o9J4UVJZycIpk=','admin','vHsstj/Qc63ZGiU8GDP7mg=='),(16,'attendee11','33MkK0N6kbOoaS34yIJ1ZpYa5/EeCDuVvj2s4fe+9Zc=','attendee','DWq+nWfycLRcq08cMgOnwA==');
/*!40000 ALTER TABLE users ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-29 23:16:57
