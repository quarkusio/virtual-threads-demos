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
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class DemoFrauds extends LitElement {
    static styles = css`
      .grid {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 10px;
      }
    
      h2 {
        font-family: "Red Hat Text", monospace;
        text-align: left;
        color: var(--main-highlight-text-color);
      }
      
      p.fraud { 
        font-size: small
      }
      
      span.field {
        font-weight: bold;
      }
      
      .clearIcon {
        font-size: small;
      }
    `;

    static properties = {
        _frauds: {state: true, type: Array},
    }

    constructor() {
        super();
        this._frauds = [];
    }


    connectedCallback() {
        super.connectedCallback();
        const source = new EventSource("/frauds");
        source.onmessage = ev => {
            const t = JSON.parse(ev.data);
            this._frauds.unshift(t);
            if (this._frauds.length > 100) {
                this._frauds.pop();
            }
            super.update(this._frauds);
        }
    }

    render() {
        return html`${until(this._render(), html`<span>Loading orders...</span>`)}`;
    }

    _render() {
        if (this._frauds) {
            return html`<div class="grid">
                    <div>
                        <h2>Detected Frauds <vaadin-button @click=${() => this._clear()} theme="tertiary small"><vaadin-icon class="clearIcon" icon="font-awesome-solid:broom"></vaadin-icon></vaadin-button></h2>
                        ${this._frauds.map(fraud => this._renderFraud(fraud))}
                    </div>
                </div>`;
        }
    }

    _renderFraud(fraud) {
        return html`
            <p class="fraud"><span class="field">Account:</span> ${fraud.account} : <span class="field">Amount:</span> <strong>${fraud.amount} €</strong></p>
        `
    }

    _clear() {
        this._frauds = [];
        super.update(this._frauds);
    }


}

customElements.define('demo-frauds', DemoFrauds);