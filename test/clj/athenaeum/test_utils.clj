(ns athenaeum.test-utils)

(defmacro with-fixtures
  [fixtures & body]
  (if (= 1 (count fixtures))
    `(~(first fixtures)
      (fn []
        ~@body))
    `(with-fixtures
       [~(first fixtures)]
       (with-fixtures ~(rest fixtures) ~@body))))
