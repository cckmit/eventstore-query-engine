CREATE TABLE domain_event_entry
(
    global_index         bigserial    NOT NULL,
    event_identifier     varchar(255) NOT NULL,
    meta_data            bytea NULL,
    payload              bytea        NOT NULL,
    payload_revision     varchar(255) NULL,
    payload_type         varchar(255) NOT NULL,
    time_stamp           varchar(255) NOT NULL,
    aggregate_identifier varchar(255) NOT NULL,
    sequence_number      int8         NOT NULL,
    "type"               varchar(255) NULL,
    CONSTRAINT gst_empty_domain_event_entry_aggregate_identifier_sequence__key UNIQUE (aggregate_identifier, sequence_number),
    CONSTRAINT gst_empty_domain_event_entry_event_identifier_key UNIQUE (event_identifier),
    CONSTRAINT gst_empty_domain_event_entry_pkey PRIMARY KEY (global_index)
);
