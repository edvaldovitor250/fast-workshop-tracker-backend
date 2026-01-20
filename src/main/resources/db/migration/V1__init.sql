CREATE TABLE colaborador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL
);

CREATE TABLE workshop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_realizacao DATE NOT NULL,
    descricao VARCHAR(500)
);

CREATE TABLE ata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workshop_id BIGINT NOT NULL,
    CONSTRAINT fk_ata_workshop FOREIGN KEY (workshop_id) REFERENCES workshop (id),
    CONSTRAINT uk_ata_workshop UNIQUE (workshop_id)
);

CREATE TABLE ata_colaborador (
    ata_id BIGINT NOT NULL,
    colaborador_id BIGINT NOT NULL,
    PRIMARY KEY (ata_id, colaborador_id),
    CONSTRAINT fk_ata_colaborador_ata FOREIGN KEY (ata_id) REFERENCES ata (id) ON DELETE CASCADE,
    CONSTRAINT fk_ata_colaborador_colaborador FOREIGN KEY (colaborador_id) REFERENCES colaborador (id)
);

CREATE INDEX idx_colaborador_nome ON colaborador (nome);
CREATE INDEX idx_workshop_nome ON workshop (nome);
CREATE INDEX idx_workshop_data_realizacao ON workshop (data_realizacao);
CREATE INDEX idx_ata_colaborador_colaborador_id ON ata_colaborador (colaborador_id);

