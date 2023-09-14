import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import '@vaadin/grid';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class DemoTitle extends LitElement {

    static styles = css`
      h1 {
        font-family: "Red Hat Mono", monospace;
        font-size: 60px;
        font-style: normal;
        font-variant: normal;
        font-weight: 700;
        line-height: 26.4px;
        color: var(--main-highlight-text-color);
      }

      .title {
        text-align: center;
        padding: 1em;
        background: var(--main-bg-color);
      }
      
      .explanation {
        margin-left: auto;
        margin-right: auto;
        width: 50%;
        text-align: justify;
        font-size: 20px;
      }
    `

    render() {
        return html`
            <div class="title">
                <h1>Fraud Detection</h1>
            </div>
            <div class="explanation">
                This demo demonstrates how to process Kafka messages using virtual threads in Quarkus.
                It generates transactions and detects frauds using a Redis time series (to introduce I/O during the processing as it leverages virtual threads' ability to block).
                This page reports the detected frauds and the current/max concurrency.
                Note that the concurrency is limited to 1024. You can increase this threshold by <a href="https://quarkus.io/guides/messaging-virtual-threads#control-the-maximum-concurrency">configuration</a>.
            </div>
        `
    }


}

customElements.define('demo-title', DemoTitle);