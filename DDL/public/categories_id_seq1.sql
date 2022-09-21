create sequence categories_id_seq1;

alter sequence categories_id_seq1 owner to root;

alter sequence categories_id_seq1 owned by categories.id;

