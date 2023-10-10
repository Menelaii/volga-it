CREATE OR REPLACE FUNCTION calculate_distance(
    lat1 DOUBLE PRECISION, lon1 DOUBLE PRECISION,
    lat2 DOUBLE PRECISION, lon2 DOUBLE PRECISION)
    RETURNS DOUBLE PRECISION AS $$
DECLARE
    R DOUBLE PRECISION := 6371.0;  -- Радиус Земли в километрах
    dlat DOUBLE PRECISION := RADIANS(lat2 - lat1);
    dlon DOUBLE PRECISION := RADIANS(lon2 - lon1);
    a DOUBLE PRECISION;
    c DOUBLE PRECISION;
BEGIN
    a := SIN(dlat / 2) * SIN(dlat / 2) +
         COS(RADIANS(lat1)) * COS(RADIANS(lat2)) *
         SIN(dlon / 2) * SIN(dlon / 2);

    c := 2 * ATAN2(SQRT(a), SQRT(1 - a));

    -- Расстояние в километрах
    RETURN R * c;
END;
$$ LANGUAGE plpgsql;
