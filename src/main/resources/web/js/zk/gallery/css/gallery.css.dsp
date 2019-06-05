 .technogalleryBigBox {
        max-width: 100%;
        height: 100%;
        float: right;
        padding: 0px;
        margin: 0px;
        background-size: 100%;
        box-sizing: border-box;
        background-repeat: no-repeat;
      }
      .gallery-carrousel > img {
        min-width: 75px;
        width: 75px;
        height: 75px;
        padding: 0px;
        margin: 1px;
        background-size: 200%;
        background-repeat: no-repeat;
        background-position: center;
        box-sizing: border-box;
      }
      .technogallery,
      .gallery-container {
        display: flex;
        flex-flow: column;
        height: 100%;
      }
      .gallery-carrousel {
        display: flex;
        flex-flow: row;
        width: 94%;
        overflow: hidden;
      }
      #left-button,
      #right-button {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 3%;
        color: whitesmoke;
      }
      .gallery-control {
        display: flex;
        align-items: center;
        height: 13%;
        background-color: black;
      }
      .gallery-viewer {
        width: 100%;
        height: 87%;
        display: flex;
        align-items: center;
        justify-content: space-around;
        background-color: black;
      }
      #prev-button,
      #next-button {
        position: absolute;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100px;
        height: 87%;
        box-sizing: border-box;
        z-index: 158200;
      }

      #prev-button:hover,
      #next-button:hover {
        background-color: rgba(0, 0, 0, 0.5);
      }
      #prev-button {
        left: 0;
      }
      #next-button {
        right: 0;
      }