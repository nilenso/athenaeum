(ns athenaeum.test-utils)

(defmacro with-fixtures
  "Use to wrap a body of code with clojure.test fixtures.
  Fixtures are applied in the same order as clojure.test/use-fixtures.
  Useful for applying fixtures around testing blocks."
  [fixtures & body]
  `((comp ~@(reverse fixtures))
    (fn []
      ~@body)))

#_(defmacro with-fixtures
  [fixtures & body]
  `(
    ~@body))

(defn fx1 [f] (prn "first") (f))
(defn fx2 [f] (prn "second") (f))

(with-fixtures [fx1 fx2] (prn "third"))
