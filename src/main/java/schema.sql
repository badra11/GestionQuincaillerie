-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: gestion_quincaillerie
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id_cat` int(11) NOT NULL AUTO_INCREMENT,
  `nom_cat` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id_cat`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id_client` int(11) NOT NULL AUTO_INCREMENT,
  `id_credit` int(11) DEFAULT NULL,
  `type_client` varchar(100) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telephone` varchar(50) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  PRIMARY KEY (`id_client`),
  UNIQUE KEY `id_credit` (`id_credit`),
  CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`id_credit`) REFERENCES `credit_ventes` (`id_credit`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commandes`
--

DROP TABLE IF EXISTS `commandes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commandes` (
  `id_commande` int(11) NOT NULL AUTO_INCREMENT,
  `num_commande` varchar(100) NOT NULL,
  `id_fournisseur` int(11) NOT NULL,
  `id_employe` int(11) NOT NULL,
  `mode_payment` varchar(100) NOT NULL,
  `montant_total` decimal(10,0) NOT NULL,
  `montant_paye` decimal(10,0) NOT NULL,
  `montant_restant` decimal(10,0) NOT NULL,
  `date_commande` datetime NOT NULL,
  `validation` tinyint(1) NOT NULL,
  `statut_payment` varchar(100) NOT NULL,
  PRIMARY KEY (`id_commande`),
  UNIQUE KEY `num_commande` (`num_commande`),
  KEY `id_employe` (`id_employe`),
  KEY `id_fournisseur` (`id_fournisseur`),
  CONSTRAINT `commandes_ibfk_1` FOREIGN KEY (`id_employe`) REFERENCES `employes` (`id_employe`),
  CONSTRAINT `commandes_ibfk_2` FOREIGN KEY (`id_fournisseur`) REFERENCES `fournisseurs` (`id_fournisseur`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credit_ventes`
--

DROP TABLE IF EXISTS `credit_ventes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit_ventes` (
  `id_credit` int(11) NOT NULL AUTO_INCREMENT,
  `num_credit` varchar(100) NOT NULL,
  `id_client` int(11) NOT NULL,
  `id_vente` int(11) NOT NULL,
  `montant_credit` decimal(10,0) NOT NULL,
  `montant_payée` decimal(10,0) NOT NULL,
  `statut` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_credit`),
  KEY `id_client` (`id_client`),
  KEY `id_vente` (`id_vente`),
  CONSTRAINT `credit_ventes_ibfk_1` FOREIGN KEY (`id_client`) REFERENCES `clients` (`id_client`),
  CONSTRAINT `credit_ventes_ibfk_2` FOREIGN KEY (`id_vente`) REFERENCES `ventes` (`id_vente`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emplacements`
--

DROP TABLE IF EXISTS `emplacements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emplacements` (
  `id_emplacement` int(11) NOT NULL AUTO_INCREMENT,
  `type_emplacement` varchar(50) NOT NULL,
  `designation_emplacement` varchar(100) NOT NULL,
  PRIMARY KEY (`id_emplacement`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employes`
--

DROP TABLE IF EXISTS `employes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employes` (
  `id_employe` int(11) NOT NULL AUTO_INCREMENT,
  `matricule` varchar(100) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `telephone` varchar(100) NOT NULL,
  `role` varchar(50) NOT NULL,
  `date_embauche` date NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salaire` decimal(10,0) NOT NULL,
  PRIMARY KEY (`id_employe`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fournisseurs`
--

DROP TABLE IF EXISTS `fournisseurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fournisseurs` (
  `id_fournisseur` int(11) NOT NULL AUTO_INCREMENT,
  `nom_societe` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telephone` varchar(100) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  PRIMARY KEY (`id_fournisseur`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journal_activite`
--

DROP TABLE IF EXISTS `journal_activite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `journal_activite` (
  `id_log` int(11) NOT NULL AUTO_INCREMENT,
  `id_employe` int(11) NOT NULL,
  `action` varchar(255) NOT NULL,
  `date_action` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_log`),
  KEY `id_employe` (`id_employe`),
  CONSTRAINT `journal_activite_ibfk_1` FOREIGN KEY (`id_employe`) REFERENCES `employes` (`id_employe`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ligne_commandes`
--

DROP TABLE IF EXISTS `ligne_commandes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ligne_commandes` (
  `id_lc` int(11) NOT NULL AUTO_INCREMENT,
  `id_commande` int(11) NOT NULL,
  `id_produit` int(11) NOT NULL,
  `quantite` int(11) NOT NULL,
  `prix_unitaire` decimal(10,0) NOT NULL,
  `total_prix` decimal(10,0) NOT NULL,
  `status_commande` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_lc`),
  KEY `id_produit` (`id_produit`),
  KEY `id_commande` (`id_commande`),
  CONSTRAINT `ligne_commandes_ibfk_1` FOREIGN KEY (`id_produit`) REFERENCES `produits` (`id_produit`),
  CONSTRAINT `ligne_commandes_ibfk_2` FOREIGN KEY (`id_commande`) REFERENCES `commandes` (`id_commande`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ligne_ventes`
--

DROP TABLE IF EXISTS `ligne_ventes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ligne_ventes` (
  `id_lv` int(11) NOT NULL AUTO_INCREMENT,
  `id_vente` int(11) NOT NULL,
  `id_produit` int(11) NOT NULL,
  `quantite` int(11) NOT NULL,
  `prix_unitaire` decimal(10,0) NOT NULL,
  `total_prix` decimal(10,0) NOT NULL,
  `statut` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_lv`),
  KEY `id_produit` (`id_produit`),
  KEY `id_vente` (`id_vente`),
  CONSTRAINT `ligne_ventes_ibfk_2` FOREIGN KEY (`id_produit`) REFERENCES `produits` (`id_produit`),
  CONSTRAINT `ligne_ventes_ibfk_3` FOREIGN KEY (`id_vente`) REFERENCES `ventes` (`id_vente`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produits`
--

DROP TABLE IF EXISTS `produits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produits` (
  `id_produit` int(11) NOT NULL AUTO_INCREMENT,
  `id_type` int(11) NOT NULL,
  `id_emplacement` int(11) NOT NULL,
  `reference` varchar(50) NOT NULL,
  `designation` varchar(100) NOT NULL,
  `code_barre` varchar(100) NOT NULL,
  `prix_vente` decimal(10,0) NOT NULL,
  `prix_achat` decimal(10,0) NOT NULL,
  `quantite` int(11) NOT NULL,
  `alerte_seuil` tinyint(1) NOT NULL,
  `unite` varchar(100) NOT NULL,
  `marque` varchar(100) NOT NULL,
  PRIMARY KEY (`id_produit`),
  KEY `id_type` (`id_type`),
  KEY `id-emplacement` (`id_emplacement`),
  CONSTRAINT `produits_ibfk_1` FOREIGN KEY (`id_type`) REFERENCES `types` (`id_type`),
  CONSTRAINT `produits_ibfk_2` FOREIGN KEY (`id_emplacement`) REFERENCES `emplacements` (`id_emplacement`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reçus_ventes`
--

DROP TABLE IF EXISTS `reçus_ventes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reçus_ventes` (
  `id_reçus` int(11) NOT NULL AUTO_INCREMENT,
  `id_vente` int(11) NOT NULL,
  `num_reçus` varchar(100) NOT NULL,
  `montant` decimal(10,0) NOT NULL,
  `date_reçus` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_reçus`),
  KEY `id_vente` (`id_vente`),
  CONSTRAINT `reçus_ventes_ibfk_1` FOREIGN KEY (`id_vente`) REFERENCES `ventes` (`id_vente`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `types` (
  `id_type` int(11) NOT NULL AUTO_INCREMENT,
  `id_cat` int(11) NOT NULL,
  `nom_type` varchar(100) NOT NULL,
  `description` varchar(3000) NOT NULL,
  PRIMARY KEY (`id_type`),
  KEY `id_cat` (`id_cat`),
  CONSTRAINT `types_ibfk_1` FOREIGN KEY (`id_cat`) REFERENCES `categories` (`id_cat`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ventes`
--

DROP TABLE IF EXISTS `ventes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ventes` (
  `id_vente` int(11) NOT NULL AUTO_INCREMENT,
  `num_vente` varchar(100) NOT NULL,
  `id_client` int(11) DEFAULT NULL,
  `id_employe` int(11) NOT NULL,
  `mode_payment` varchar(100) NOT NULL,
  `montant_total` decimal(10,0) NOT NULL,
  `montant_reçus` decimal(10,0) NOT NULL,
  `montant_restant` decimal(10,0) NOT NULL,
  `date_vente` datetime NOT NULL DEFAULT current_timestamp(),
  `validation` tinyint(1) NOT NULL,
  `adresse_livraison` varchar(500) DEFAULT NULL,
  `vehicule_livraison` varchar(100) DEFAULT NULL,
  `statut_payment` varchar(100) NOT NULL,
  PRIMARY KEY (`id_vente`),
  UNIQUE KEY `num_vte` (`num_vente`),
  KEY `id_employe` (`id_employe`),
  KEY `id_client` (`id_client`),
  CONSTRAINT `ventes_ibfk_2` FOREIGN KEY (`id_employe`) REFERENCES `employes` (`id_employe`),
  CONSTRAINT `ventes_ibfk_3` FOREIGN KEY (`id_client`) REFERENCES `clients` (`id_client`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

INSERT INTO employes (matricule, nom, prenom, telephone, role, date_embauche, username, password, salaire)
VALUES ('EMP-001-2026-ADAD', 'Admin', 'Administrateur', '00000000', 'admin', CURDATE(), 'admin', '12345678', 0);

-- Dump completed on 2026-07-29  9:55:00
