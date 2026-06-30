CREATE TABLE order_items (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             unit_price DECIMAL(10,2) NOT NULL,
                             quantity INT NOT NULL,
                             total_price DECIMAL(10,2) NOT NULL,

                             CONSTRAINT pk_order_items
                                 PRIMARY KEY (id),

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders (id),

                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id)
                                     REFERENCES products (id)
);