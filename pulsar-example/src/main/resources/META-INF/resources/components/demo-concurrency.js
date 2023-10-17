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

    static properties = {
        _current: {state: true, type: Number},
        _max: {state: true, type: Number},
        _transactions: {state: true, type: Number},
        _frauds: {state: true, type: Number}
    }

    constructor() {
        super();
        this._current = 0;
        this._max = 0;
        this._transactions = 0;
        this._frauds = 0;
    }

    render() {
        return html`
            <div class="concurrency">
                <p>Number of transactions: ${this._transactions}</p>
                <p>Number of detected frauds: ${this._frauds}</p>
                <p>Current concurrency: ${this._current}</p>
                <p>Max concurrency: ${this._max}</p>
            </div>`;
    }

    connectedCallback() {
        super.connectedCallback();
        const source = new EventSource("/observability");
        source.onmessage = ev => {
            const t = JSON.parse(ev.data);
            this._max = t.m;
            this._current = t.c;
            this._frauds = t.f;
            this._transactions = t.tx;
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