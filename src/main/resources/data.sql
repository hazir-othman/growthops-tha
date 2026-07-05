-- 1. Create a unique constraint index so duplicates don't break subsequent restarts
CREATE UNIQUE INDEX IF NOT EXISTS idx_rate_unique 
ON insurance_rates (coverage_type, area, plan_type);

-- 2. Populate the table safely using SQLite's INSERT OR IGNORE syntax
INSERT OR IGNORE INTO insurance_rates (coverage_type, area, plan_type, rate, is_flat_rate) VALUES
-- Single Trip Rates (Per Day)
('SINGLE', 'AREA 1', 'PLAN A', 10.00, 0),
('SINGLE', 'AREA 1', 'PLAN B', 20.00, 0),
('SINGLE', 'AREA 2', 'PLAN A', 15.00, 0),
('SINGLE', 'AREA 2', 'PLAN B', 30.00, 0),
('SINGLE', 'AREA 3', 'PLAN A', 20.00, 0),
('SINGLE', 'AREA 3', 'PLAN B', 40.00, 0),
('SINGLE', 'AREA 4', 'PLAN A', 5.00,  0),
('SINGLE', 'AREA 4', 'PLAN B', 10.00, 0),

-- Annual Plans (Flat Rates)
('ANNUAL', 'AREA 1', 'PLAN A', 100.00, 1),
('ANNUAL', 'AREA 1', 'PLAN B', 150.00, 1),
('ANNUAL', 'AREA 2', 'PLAN A', 150.00, 1),
('ANNUAL', 'AREA 2', 'PLAN B', 200.00, 1),
('ANNUAL', 'AREA 3', 'PLAN A', 200.00, 1),
('ANNUAL', 'AREA 3', 'PLAN B', 250.00, 1);