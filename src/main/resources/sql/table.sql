CREATE TABLE `customer`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
   `contact` varchar(255) DEFAULT NULL,
   `telephone` varchar(255) DEFAULT NULL,
   `email` varchar(255) DEFAULT null,
   `remark` text,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
