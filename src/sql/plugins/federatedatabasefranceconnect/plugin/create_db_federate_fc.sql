--
-- Table struture for federate_fc_link
--
DROP TABLE IF EXISTS federate_fc_link;
CREATE TABLE federate_fc_link (
    lutece_user_name varchar(255)DEFAULT '' NOT NULL,
  	federate_key varchar(255)DEFAULT '' NOT NULL,
  	PRIMARY KEY (lutece_user_name,federate_key)
  );