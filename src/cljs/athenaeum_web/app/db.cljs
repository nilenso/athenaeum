(ns athenaeum-web.app.db)

(def default-db
  {:login-state :logged-out
   :page        {:handler :home-page}
   :auth2-loaded false})
