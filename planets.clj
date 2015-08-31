(defn check [expect input return]
  (if (= expect input) return (throw (Exception. (str "Measurements don't line up: " expect input return)))))

(defn times-100 [[magnitude units]]
  [(* magnitude 100) units])

(defn double [[magnitude units]]
  [(* magnitude 2) units])

(defn pixels-to-inches [[magnitude units]]
    [(/ magnitude 326) (check :px-squared units :in-squared)])

(defn inches-to-feet [[magnitude units]]
    [(/ magnitude 12) (check :in-squared units :ft-squared)])

(defn feet-to-acres [[magnitude units]]
  [(/ magnitude 43560) (check :ft-squared units :acres)])

(defn acres-to-miles [[magnitude units]]
  [(/ magnitude 680) (check :acres units :mi-squared)])

(defn miles-to-kilometers [[magnitude units]]
  [(* magnitude 1.60934) (check :mi-squared units :km-squared)])

(defn radius-given-volume [[magnitude units]]
  [(Math/cbrt (/ (/ magnitude (/ 4 3)) Math/PI)) (check :m-cubed units :m)])

(defn surface-area-given-radius [[magnitude units]]
  [(* 4 Math/PI magnitude magnitude) (check :m units :m-squared)])

(defn meters-to-kilometers [[magnitude units]]
  [(/ magnitude 1000) (check :m-squared units :km-squared)])

(defn kilometers-to-meters [[magnitude units]]
  [(* magnitude 1000) (check :m-squared units :km-squared)])

(defn kilometers-to-solar [[magnitude units]]
  [(/ magnitude 6.955e5) (check :km units :sol)])

(defn solar-to-meters [[magnitude units]]
  [(* magnitude 6.955e5 1000) (check :sol units :m)])

(defn circumference [[diameter units]]
  [(* Math/PI diameter) units])

(defn area-circle [[diameter units]]
  (let [radius (/ diameter 2)]
    [(* Math/PI radius radius) :m-squared]))

(defn area-rectangle [[length l-units] [width w-units]]
  [(* length width) :m-squared])

(defn sum-measurments [& rest]
  [(reduce + (map first rest)) (last (first rest))])

(defn divide [& rest]
  [(reduce / (map first rest)) (last (first rest))])

(defn cylinder-surface [specs]
  (let [width (circumference (:diameter specs))
        top (area-circle (:diameter specs))
        bottom (area-circle (:diameter specs))]
    (sum-measurments (area-rectangle (:height specs) width)
                     top
                     bottom)))


(sum-measurments [50 :m] [30 :m])

;;1.711

(def little-timmy [0.5 :px-squared])

(def thumbnail (times-100 little-timmy))

(def pizza (pixels-to-inches (times-100 thumbnail)))

(def obese-noble-circle (inches-to-feet (times-100 pizza)))

(def notches-mansion-half (times-100 obese-noble-circle))

(def thirty-football-fields (feet-to-acres (times-100 notches-mansion-half)))

(def three-and-a-half-centeral-parks (times-100 thirty-football-fields))

(def las-angeles (acres-to-miles (times-100 three-and-a-half-centeral-parks)))

(def greece (times-100 las-angeles))

(def between-europe-and-antartica (times-100 greece))

(def two-earths (times-100 between-europe-and-antartica))

(def two-jupiters (miles-to-kilometers (times-100 two-earths)))

(def sun (times-100 two-jupiters))

(def a-little-larger-than-sirius (times-100 sun))

(def a-little-smaller-than-north-star (times-100 a-little-larger-than-sirius))

(def milky-way-blackhole (->> [1.27e10 :m]
                              surface-area-given-radius
                              meters-to-kilometers))

(def milky-way (->> {:diameter [9.46e20 :m] :height [9.46e18 :m]}
                    cylinder-surface
                    meters-to-kilometers))

(def wat (times-100 milky-way-blackhole))

(def actual-alcyone (->> [8.2 :sol]
                         solar-to-meters
                         surface-area-given-radius
                         meters-to-kilometers))

(def actual-sirius (->> [1.711 :sol]
                        solar-to-meters
                        surface-area-given-radius
                        meters-to-kilometers))

(def actual-north-star (->> [46 :sol]
                            solar-to-meters
                            surface-area-given-radius
                            meters-to-kilometers))

((fn [] (let [target actual-north-star] [wat target (/ (first target) (first wat))])))

(str greece)

[
 ;part one
 little-timmy
 thumbnail
 pizza
 obese-noble-circle
 notches-mansion-half
 thirty-football-fields
 three-and-a-half-centeral-parks
 las-angeles
 greece
 between-europe-and-antartica
 "part two"
 ;;part two
 two-earths
 two-jupiters
 sun
 a-little-larger-than-sirius
 a-little-smaller-than-north-star
 milky-way-blackhole
 ;;part three
 milky-way]

(divide milky-way milky-way-blackhole)


[[0.5 :px-squared]
 [50.0 :px-squared]
 [15.337423312883436 :in-squared]
 [127.8118609406953 :ft-squared]
 [12781.18609406953 :ft-squared]
 [29.34156587251958 :acres]
 [2934.156587251958 :acres]
 [431.49361577234674 :mi-squared]
 [43149.361577234675 :mi-squared]
 [4314936.157723468 :mi-squared]
 "part two"
 [4.314936157723468E8 :mi-squared]
 [6.944199356070686E10 :km-squared]
 [6.944199356070687E12 :km-squared]
 [6.944199356070686E14 :km-squared]
 [6.9441993560706864E16 :km-squared]
 [2.02682991638999066E18 :km-squared]
 [1.433845380901781E39 :km-squared]]
