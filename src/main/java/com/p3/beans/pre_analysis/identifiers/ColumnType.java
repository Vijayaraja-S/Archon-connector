package com.p3.beans.pre_analysis.identifiers;

import java.io.Serializable;

public enum ColumnType implements Serializable {
    NORMAL,
    PRIMARY,
    FOREIGN,
    PRIMARY_COMPOSITE,
    FOREIGN_COMPOSITE, COMPLEX;
}
