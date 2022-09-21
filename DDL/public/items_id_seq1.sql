create sequence items_id_seq1;

alter sequence items_id_seq1 owner to root;

alter sequence items_id_seq1 owned by items.id;

