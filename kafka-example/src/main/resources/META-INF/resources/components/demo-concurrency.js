import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import {until} from 'lit/directives/until.js';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class DemoGenerateButton extends LitElement {
    static styles = css`
      .concurrency {
        margin-right: 5px;
        margin-bottom: 5px;
        font-weight: bold;
      }
    `;

    constructor() {
        super();
    }

    static properties = {
        current: {type: Number},
        max: {type: Number},
        transactions: {type: Number},
        frauds: {type: Number}
    }

    render() {
        return html`
            <div class="concurrency">
                <p>Number of transactions: ${this.transactions}</p>
                <p>Number of detected frauds: ${this.frauds}</p>
                <p>Current concurrency: ${this.current}</p>
                <p>Max concurrency: ${this.max}</p>
            </div>`;
    }

    connectedCallback() {
        super.connectedCallback();
        this.max = 0;
        this.current = 0;
        this.frauds = 0;
        this.transactions = 0;
        const source = new EventSource("/observability");
        source.onmessage = ev => {
            const  t = JSON.parse(ev.data);
            this.max = t.m;
            this.current = t.c;
            this.frauds = t.f;
            this.transactions = t.tx;
        }
    }

    _generate() {
        fetch("/transactions?count=2000", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
    }


}

customElements.define('demo-concurrency', DemoGenerateButton);