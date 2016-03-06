(ns cheekyled.crash
  (:gen-class)
  (:import [org.hid4java HidDevice HidDeviceManager HidManager HidServices HidServicesListener]
           [org.hid4java.event HidServicesEvent]))

(defn hid-services []
  (let [p (proxy [HidServicesListener] []
          (hidDeviceAttached [e] (prn 'ATTACHED e))
          (hidDeviceDetached [e] (prn 'DETACHED e))
          (hidFailure [e] (prn 'FAILURE e)))
        services (HidManager/getHidServices)]
    (.addHidServicesListener services p)
    services))

(defn attached-devices []
  (.getAttachedHidDevices (hid-services)))

(defn write [device message packet-length report-id]
 (.write device message packet-length (byte report-id)))


(defn find-led []
  (let [vendor 0x1d34
        device 0x0013]
    (.getHidDevice (hid-services) vendor device nil)))

(defn with-led [f]
  (let [led (find-led)]
    (f led)
    (.close led)))

(defn write-diamond [led]
  (let [diamond [[0x00, 0x00, 0xFF,0xFE,0xFF, 0xFF,0xFD,0x7F,],
                 [0x00, 0x02, 0xFF,0xFB,0xBF, 0xFF,0xF7,0xDF,],
                 [0x00, 0x04, 0xFF,0xFB,0xBF, 0xFF,0xFD,0x7F,],
                 [0x00, 0x06, 0xFF,0xFE,0xFF,]]]
    (doseq [row diamond]
      (write led (byte-array row) 8 0))))
