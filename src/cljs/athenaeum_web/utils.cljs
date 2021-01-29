(ns athenaeum-web.utils)

(defn map-id-to-value
  [values]
  (zipmap (map :id values) values))
