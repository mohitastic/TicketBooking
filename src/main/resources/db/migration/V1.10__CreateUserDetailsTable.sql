CREATE TABLE USER_DETAILS(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    USER_ID BIGINT,
    NAME VARCHAR(50),
    DOB DATE,
    EMAIL_ADDRESS VARCHAR(50),
    MOBILE_NUMBER VARCHAR(10),
    CONSTRAINT fk_user
    FOREIGN KEY (USER_ID) REFERENCES USERTABLE (ID)
)